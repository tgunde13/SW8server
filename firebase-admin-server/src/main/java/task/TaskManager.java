package task;

import com.google.firebase.database.*;
import com.google.firebase.tasks.OnFailureListener;
import com.google.firebase.tasks.OnSuccessListener;
import firebase.FirebaseNodes;
import model.Player;
import model.PlayerMinion;
import model.Zone;

import java.util.ArrayList;

/**
 * Manage requests from clients
 */
public class TaskManager {

    /**
     * Starts the request management by listening to relevant Firebase nodes.
     */
    public static void start() {
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseNodes.TASKS).child(FirebaseNodes.REQUESTS);

        reference.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("task.TaskManager.onChildAdded, previousChildName: " + previousChildName);
                onNewTask(dataSnapshot);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("task.TaskManager.onChildChanged, previousChildName: " + previousChildName);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("task.TaskManager.onChildRemoved");
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("task.TaskManager.onChildMoved, previousChildName: " + previousChildName);
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("task.TaskManager.onCancelled");
            }
        });

        System.out.println("TOB: task.TaskManager listening started");
    }

    /**
     * Handles a new task
     * @param snapshot Firebase snapshot of the request
     */
    private static void onNewTask(DataSnapshot snapshot) {
        // Get request code
        int requestCode;
        try {
            requestCode = (int) (long) (Long) snapshot.child(FirebaseNodes.STATUS_CODE).getValue();
        } catch (ClassCastException e) {
            String userId = snapshot.getKey();
            ResponseHandler.respond(userId, HttpCodes.HTTP_BAD_REQUEST);
            return;
        }

        // Call server dependent of request code
        switch (requestCode) {
            case 1:  configureAvatar(snapshot);
                break;
            case 2:  setPosition(snapshot);
                break;
            default: System.out.println("TOB: task.TaskManager, onNewTask, default");
                break;
        }
    }

    /**
     * Configures an avatar.
     * Fields: REQUEST_DATA: Avatar name.
     * @param snapshot Firebase snapshot of the request
     */
    private static void configureAvatar(DataSnapshot snapshot) {
        final String userId = snapshot.getKey();

        // Get desired avatar name
        final String name;
        try {
            name = (String) snapshot.child(FirebaseNodes.REQUEST_DATA).getValue();
        } catch (ClassCastException e) {
            ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
            return;
        }

        // Check for existing name
        // TODO Lock
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.NAME_INDEX).child(name)
        .addListenerForSingleValueEvent(new ValueEventListener() {
            public void onCancelled(DatabaseError databaseError) {
                ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
            }

            public void onDataChange(DataSnapshot dataSnapshot) {
                // If name is already in use
                if (dataSnapshot.exists()) {
                    ResponseHandler.respond(userId, HttpCodes.HTTP_CONFLICT);
                    return;
                }

                // Create minion for player
                PlayerMinion minion = new PlayerMinion();
                minion.type = "Starter";
                minion.maxHealth = 10;
                minion.currentHealth = minion.maxHealth;
                minion.level = 1;
                minion.xp = 0;

                // Create player and add minion
                Player player = new Player();
                player.id = 1;
                player.name = name;
                player.minions = new ArrayList<PlayerMinion>();
                player.minions.add(minion);

                // Upload player to Firebase
                FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(userId)
                .setValue(player).addOnFailureListener(new TaskFailureListener(userId))
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    public void onSuccess(Void aVoid) {
                        // Update name index
                        FirebaseDatabase.getInstance().getReference(FirebaseNodes.NAME_INDEX).child(name)
                        .setValue(userId).addOnFailureListener(new TaskFailureListener(userId))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void aVoid) {
                                ResponseHandler.respond(userId, HttpCodes.HTTP_OK);
                            }
                        });
                    }
                });
            }
        });
    }

    /**
     * Sets the position of a player.
     * Fields: REQUEST_DATA: zone of the player (of type Zone).
     * @param snapshot Firebase snapshot of the request
     */
    private static void setPosition(DataSnapshot snapshot) {
        final String userId = snapshot.getKey();
        final Zone newZone = snapshot.child(FirebaseNodes.REQUEST_DATA).getValue(Zone.class);

        final DatabaseReference zoneIndexRef = FirebaseDatabase.getInstance()
                                               .getReference(FirebaseNodes.PLAYER_ZONE_INDEX);

        // Add to player zone index
        zoneIndexRef.child(String.valueOf(newZone.getLatIndex())).child(String.valueOf(newZone.getLonIndex()))
        .push().setValue(userId).addOnFailureListener(new TaskFailureListener(userId))
        .addOnSuccessListener(new OnSuccessListener<Void>() {
            public void onSuccess(Void aVoid) {
                final DatabaseReference playerZoneRef = FirebaseDatabase.getInstance()
                                                        .getReference(FirebaseNodes.PLAYERS)
                                                        .child(userId).child(FirebaseNodes.PLAYER_ZONE);

                // Get old player zone (if one exists)
                playerZoneRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    public void onCancelled(DatabaseError databaseError) {
                        ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
                    }

                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final Zone oldZone = dataSnapshot.getValue(Zone.class);

                        // Set player zone to new zone
                        playerZoneRef.setValue(newZone).addOnFailureListener(new TaskFailureListener(userId))
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            public void onSuccess(Void aVoid) {
                                // Remove player from old player zone index entry, if exists
                                if (oldZone != null) {
                                    zoneIndexRef.child(String.valueOf(oldZone.getLatIndex()))
                                    .child(String.valueOf(oldZone.getLonIndex())).removeValue()
                                    .addOnFailureListener(new TaskFailureListener(userId))
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        public void onSuccess(Void aVoid) {
                                            ResponseHandler.respond(userId, HttpCodes.HTTP_OK);
                                        }
                                    });
                                } else {
                                    ResponseHandler.respond(userId, HttpCodes.HTTP_OK);
                                }
                            }
                        });
                    }
                });
            }
        });
    }
}
