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
        int requestCode = (int) (long) (Long) snapshot.child(FirebaseNodes.STATUS_CODE_NODE).getValue();

        switch (requestCode) {
            case 1:  configureAvatar(userId);
                break;
            default: System.out.println("TOB: TaskManager, onNewTask, default");
                break;
        }
    }

    /**
     * Configures an avatar
     * @param userId Firebase user id of the player requesting
     */
    private static void configureAvatar(final String userId) {
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
        player.name = "None";
        player.minions = new ArrayList<PlayerMinion>();
        player.minions.add(minion);

        // Upload player to Firebase
        FirebaseDatabase.getInstance().getReference().child(FirebaseNodes.PLAYERS_NODE).child(userId)
        .setValue(player, new DatabaseReference.CompletionListener() {
            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                // Check for error
                if (databaseError != null) {
                    ResponseHandler.respond(userId, HttpCodes.HTTP_INTERNAL_SERVER_ERROR);
                    return;
                }

                // Remove request
                ResponseHandler.respond(userId, HttpCodes.HTTP_OK);
            }
        });
    }
}
