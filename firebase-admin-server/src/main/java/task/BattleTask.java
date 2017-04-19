package task;

import com.google.firebase.database.DataSnapshot;
import model.BattleState;

/**
 * Created by Tobias on 17/04/2017.
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
