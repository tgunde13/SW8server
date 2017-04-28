package task;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;

/**
 * Manage requests from clients
 */
public class TaskManager {

    /**
     * Starts the request management by listening to relevant Firebase nodes.
     */
    public static void start() {
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.TASKS).child(FirebaseNodes.REQUESTS)
                .addChildEventListener(new ChildEventListener() {
            public void onChildAdded(final DataSnapshot dataSnapshot, final String previousChildName) {
                onNewTask(dataSnapshot);
            }

            public void onChildChanged(final DataSnapshot dataSnapshot, final String previousChildName) {
                System.out.println("task.TaskManager.onChildChanged, previousChildName: " + previousChildName);
            }

            public void onChildRemoved(final DataSnapshot dataSnapshot) {
            }

            public void onChildMoved(final DataSnapshot dataSnapshot, final String previousChildName) {
                System.out.println("task.TaskManager.onChildMoved, previousChildName: " + previousChildName);
            }

            public void onCancelled(final DatabaseError databaseError) {
                System.out.println("task.TaskManager.onCancelled");
            }
        });

        System.out.println("TOB: task.TaskManager listening started");
    }

    /**
     * Handles a new task
     * @param snapshot Firebase snapshot of the request
     */
    private static void onNewTask(final DataSnapshot snapshot) {
        // Get request code
        final int requestCode;
        try {
            requestCode = (int) (long) (Long) snapshot.child(FirebaseNodes.TASK_CODE).getValue();
        } catch (final ClassCastException e) {
            final String userId = snapshot.getKey();
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        // Call server dependent of request code
        switch (requestCode) {
            case 1:  new ConfigureAvatarTask(snapshot).run();
                break;
            case 2:  new SetZoneTask(snapshot, true).run();
                break;
            case 3:  new SetZoneTask(snapshot, false).run();
                break;
            case 4:  new SoloPveBattleTask(snapshot).run();
                break;
            default: System.out.println("TOB: task.TaskManager, onNewTask, default");
                break;
        }
    }
}
