package task;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;

/**
 * Starts a solo PvE battle
 *
 * Fields: REQUEST_DATA: zone of the player (of type Zone).
 *
 * Expected response codes:
 * OK
 */
class SoloPveBattleTask extends BattleTask {
    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * Creates a failure listener
     * @param snapshot Firebase snapshot of the request
     */
    SoloPveBattleTask(DataSnapshot snapshot) {
        super(snapshot);
    }

    @Override
    void run() {
        // Read player status
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS)
        .child(userId).child(FirebaseNodes.PLAYER_STATUS).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(MutableData data) {
                if (battleAllowed(data)) {
                    data.setValue(FirebaseValues.PLAYER_BATTLE);
                    return Transaction.success(data);
                } else {
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(DatabaseError error, boolean committed, DataSnapshot snapshot) {
                if (error != null) {
                    ResponseHandler.respond(userId, HttpCodes.INTERNAL_SERVER_ERROR);
                    return;
                }

                // if aborted because player was already in a battle
                if (!committed) {
                    ResponseHandler.respond(userId, HttpCodes.CONFLICT);
                    return;
                }
            }
        });
    }

    private static boolean battleAllowed(final MutableData data) {
        final String status = data.getValue(String.class);

        return status == null || status.equals(FirebaseValues.PLAYER_INVITED) || status.equals(FirebaseValues.PLAYER_INVITING);
    }
}
