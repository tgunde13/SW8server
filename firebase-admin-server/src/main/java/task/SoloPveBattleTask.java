package task;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.MapMinion;
import model.Zone;

import java.util.function.Consumer;

/**
 * Starts a solo PvE battle
 *
 * Fields:
 * REQUEST_ZONE - Zone: Zone of the environment squad to fight
 * REQUEST_KEY - String: Key of the environment squad to fight
 *
 * Expected response codes:
 * OK: Battle started
 * NOT_FOUND: Environment squad not found (e.g. if squad has expired)
 */
class SoloPveBattleTask extends BattleTask {
    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * Creates a failure listener
     * @param snapshot Firebase snapshot of the request
     */
    SoloPveBattleTask(final DataSnapshot snapshot) {
        super(snapshot);
    }

    @Override
    void run() {
    }

    /**
     * @param action
     */
    private void getEnvironmentSquad(final Consumer<MapMinion> action) {
        final Zone zone;
        final String key;

        // Get zone from request
        try {
            // TODO Test
            zone = snapshot.child(FirebaseNodes.REQUEST_ZONE).getValue(Zone.class);
            key = snapshot.child(FirebaseNodes.REQUEST_KEY).getValue(String.class);
        } catch (final DatabaseException e) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        FirebaseDatabase.getInstance().getReference(FirebaseNodes.ENVIRONMENT_SQUADS)
        .child(String.valueOf(zone.getLatIndex())).child(String.valueOf(zone.getLonIndex())).child(key)
        .addListenerForSingleValueEvent(new HandledValueEventListener(userId, dataSnapshot -> {
            if (dataSnapshot.exists()) {
                action.accept(dataSnapshot.getValue(MapMinion.class));
            } else {
                ResponseHandler.respond(userId, HttpCodes.NOT_FOUND);
            }
        }));
    }

    /**
     * @param action
     */
    private void setPlayerStatus(final Runnable action) {
        // Read player status
        FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS)
        .child(userId).child(FirebaseNodes.PLAYER_STATUS).runTransaction(new Transaction.Handler() {
            @Override
            public Transaction.Result doTransaction(final MutableData data) {
                // If battle is allowed, set battle status
                if (battleAllowed(data)) {
                    data.setValue(FirebaseValues.PLAYER_BATTLE);
                    return Transaction.success(data);
                } else {
                    return Transaction.abort();
                }
            }

            @Override
            public void onComplete(final DatabaseError error, final boolean committed, final DataSnapshot snapshot) {
                if (error != null) {
                    ResponseHandler.respond(userId, HttpCodes.INTERNAL_SERVER_ERROR);
                    return;
                }

                // if aborted because player was already in a battle
                if (!committed) {
                    ResponseHandler.respond(userId, HttpCodes.CONFLICT);
                    return;
                }

                action.run();
            }
        });
    }

    /**
     * @param data
     * @return
     */
    private static boolean battleAllowed(final MutableData data) {
        final String status = data.getValue(String.class);

        return status == null || status.equals(FirebaseValues.PLAYER_INVITED) || status.equals(FirebaseValues.PLAYER_INVITING);
    }
}
