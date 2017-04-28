package task;

import com.google.firebase.database.DataSnapshot;

/**
 * Starts a battle.
 */
abstract class BattleTask extends Task {
    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * Creates a failure listener
     * @param snapshot Firebase snapshot of the request
     */
    BattleTask(final DataSnapshot snapshot) {
        super(snapshot);
    }
}
