import com.google.firebase.database.*;

import java.util.ArrayList;

/**
 * Manage requests from clients
 */
class TaskManager {

    /**
     * Starts the request management by listening to relevant Firebase nodes.
     */
    static void start() {

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child(FirebaseNodes.TASKS_NODE).child(FirebaseNodes.REQUESTS_NODE);

        reference.addChildEventListener(new ChildEventListener() {
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("TaskManager.onChildAdded, previousChildName: " + previousChildName);
                onNewTask(dataSnapshot);
            }

            public void onChildChanged(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("TaskManager.onChildChanged, previousChildName: " + previousChildName);
            }

            public void onChildRemoved(DataSnapshot dataSnapshot) {
                System.out.println("TaskManager.onChildRemoved");
            }

            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                System.out.println("TaskManager.onChildMoved, previousChildName: " + previousChildName);
            }

            public void onCancelled(DatabaseError databaseError) {
                System.out.println("TaskManager.onCancelled");
            }
        });

        System.out.println("TOB: TaskManager listening started");
    }

    /**
     * Handles a new task
     * @param snapshot Firebase snapshot of the request
     */
    private static void onNewTask(DataSnapshot snapshot) {
        String userId = snapshot.getKey();
        int requestCode;

        try {
            requestCode = (int) (long) (Long) snapshot.child(FirebaseNodes.STATUS_CODE_NODE).getValue();
        } catch (ClassCastException e) {
            ResponseHandler.respond(userId, HttpCodes.HTTP_BAD_REQUEST);
            return;
        }

        switch (requestCode) {
            case 1:  configureAvatar(snapshot);
                break;
            default: System.out.println("TOB: TaskManager, onNewTask, default");
                break;
        }
    }

    /**
     * Configures an avatar
     * Fields: PLAYERS_DATA: Avatar name
     * @param snapshot Firebase snapshot of the request
     */
    private static void configureAvatar(DataSnapshot snapshot) {
        final String userId = snapshot.getKey();

        // Get desired avatar name
        final String name;
        try {
            name = (String) snapshot.child(FirebaseNodes.PLAYERS_DATA).getValue();
        } catch (ClassCastException e) {
            ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
            return;
        }

        // Check for existing name
        // TODO Lock
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.NAMES).child(name).addListenerForSingleValueEvent(new ValueEventListener() {
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
                FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS_NODE).child(userId)
                .setValue(player, new DatabaseReference.CompletionListener() {
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                        // Check for error
                        if (databaseError != null) {
                            ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
                            return;
                        }

                        // Update name index
                        FirebaseDatabase.getInstance().getReference(FirebaseNodes.NAMES).child(name).setValue(userId, new DatabaseReference.CompletionListener() {
                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                // Check for error
                                if (databaseError != null) {
                                    ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
                                    return;
                                }

                                ResponseHandler.respond(userId, HttpCodes.HTTP_OK);
                            }
                        });
                    }
                });
            }

            public void onCancelled(DatabaseError databaseError) {
                ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
            }
        });
    }
}
