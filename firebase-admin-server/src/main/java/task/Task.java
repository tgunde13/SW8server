package task;

import com.google.firebase.database.DataSnapshot;

/**
 * A task to be performed.
 */
abstract class Task {
    final String userId;
    final DataSnapshot snapshot;
    final TaskFailureListener failureListener;

    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * Creates a failure listener
     * @param snapshot Firebase snapshot of the request
     */
    Task(final DataSnapshot snapshot) {
        userId = snapshot.getKey();

        this.snapshot = snapshot;

        failureListener = new TaskFailureListener(userId);

        System.out.println("task.Task: " + this.getClass().getSimpleName());
    }

    /**
     * Performs this task.
     */
    abstract void run();
}
