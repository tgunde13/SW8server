package task;

import com.google.firebase.database.DataSnapshot;

/**
 * A task to be performed.
 */
abstract class Task {
    final String userId;
    final DataSnapshot snapshot;

    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * @param snapshot Firebase snapshot of the request
     */
    Task(final DataSnapshot snapshot) {
        userId = snapshot.getKey();

        this.snapshot = snapshot;

        System.out.println("task.Task: " + this.getClass().getSimpleName());
    }

    /**
     * Performs this task.
     */
    abstract void perform();
}
