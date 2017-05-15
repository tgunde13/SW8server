package task;

import battle.BattleSession;
import com.google.firebase.database.*;
import firebase.DataChangeListenerAdapter;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.*;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Starts a solo PvE battle
 *
 * Fields:
 * TASK_ZONE - Zone: Zone of the environment squad to fight
 * TASK_KEY - String: Key of the environment squad to fight
 *
 * Expected response codes:
 * OK: Battle started
 * NOT_FOUND: Environment squad not found (e.g. if squad has expired)
 */
class SoloPveBattleTask extends BattleTask {
    private static final int MAX_MINIONS = 3;

    /**
     * Extracts the user id of the client.
     * Stores the snapshot.
     * Creates a failure listener.
     * @param snapshot Firebase snapshot of the request
     */
    SoloPveBattleTask(final DataSnapshot snapshot) {
        super(snapshot);
    }

    @Override
    void run() {
        getEnvironmentSquad((key, eMinion) -> setPlayerStatus(() -> {
            final Map<String, BattleAvatar> playerTeam = new HashMap<>();
            playerTeam.put(userId, new BattleAvatar());

            final Map<String, BattleAvatar> eTeam =     new HashMap<>();
            eTeam.put(key, new BattleAvatar(eMinion));

            final BattleState state = new BattleState(playerTeam, eTeam);

            final BattleSession session = new BattleSession(state, MAX_MINIONS);
            session.start();

            final Map<String, Object> map = new HashMap<>();
            map.put(FirebaseNodes.TASK_CODE, HttpCodes.OK);
            map.put(FirebaseNodes.TASK_DATA, session.getKey());

            ResponseHandler.respond(userId, map);
        }));
    }

    /**
     * Gets the environments squad to fight with from Firebase.
     * If data from request is invalid, a BAD_REQUEST is responded.
     * If environment squad is not found, a NOT_FOUND is responded.
     * @param action action to call if successful
     */
    private void getEnvironmentSquad(final BiConsumer<String, EMinionTemplate> action) {
        final Zone zone;
        final String key;

        // Get zone from request
        try {
            zone = snapshot.child(FirebaseNodes.TASK_ZONE).getValue(Zone.class);
            key = snapshot.child(FirebaseNodes.TASK_KEY).getValue(String.class);
        } catch (final DatabaseException e) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        if (zone == null || key == null) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        FirebaseDatabase.getInstance().getReference(FirebaseNodes.ENVIRONMENT_SQUADS)
        .child(String.valueOf(zone.getLatIndex())).child(String.valueOf(zone.getLonIndex())).child(key)
        .addListenerForSingleValueEvent(new DataChangeListenerAdapter(dataSnapshot -> {
            if (dataSnapshot.exists()) {
                action.accept(key, dataSnapshot.getValue(EMinionTemplate.class));
            } else {
                ResponseHandler.respond(userId, HttpCodes.NOT_FOUND);
            }
        }));
    }

    /**
     * If player is allowed to fight, sets the player status to PLAYER_BATTLE.
     * If not allowed, a CONFLICT is responded.
     * @param action action to call if successful
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
     * Gets if battle is allowed.
     * A battle is allowed if the player status is null, PLAYER_INVITED, or PLAYER_INVITING.
     * @param data status to check with
     * @return true if, and only if, battle is allowed
     */
    private static boolean battleAllowed(final MutableData data) {
        final String status = data.getValue(String.class);

        return status == null || status.equals(FirebaseValues.PLAYER_INVITED) || status.equals(FirebaseValues.PLAYER_INVITING);
    }
}
