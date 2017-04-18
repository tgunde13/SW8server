package task;

import battle.BattleSession;
import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Starts a solo PvE battle
 *
 * Fields:
 * TASK_ZONE - Zone: Zone of the environment squad to fight
 * TASK_KEY - String: Key of the environment squad to fight
 * TASK_MINIONS - List<String>: MIN_MINIONS to MAX_MINIONS keys to player minions to fight with
 *
 * Expected response codes:
 * OK: Battle started
 * NOT_FOUND: Environment squad not found (e.g. if squad has expired)
 */
class SoloPveBattleTask extends BattleTask {
    private static final int MIN_MINIONS = 1;
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
        getEnvironmentSquad(eMinion -> setPlayerStatus(() -> {
            final List<BattleAvatar> playerTeam = new ArrayList<>();
            playerTeam.add(new BattleAvatar(new ArrayList<>(), userId));

            final List<BattleAvatar> eTeam = new ArrayList<>();
            eTeam.add(new BattleAvatar(eMinion));

            final BattleState state = new BattleState(playerTeam, eTeam);

            final BattleSession session = new BattleSession(state);
            session.start();

            final Map<String, Object> map = new HashMap<>();
            map.put(FirebaseNodes.TASK_CODE, HttpCodes.OK);
            map.put(FirebaseNodes.TASK_DATA, session.getKey());

            ResponseHandler.respond(userId, map);
        }));
    }

    /**
     * @param action
     */
    private void getEnvironmentSquad(final Consumer<EMinion> action) {
        final Zone zone;
        final String key;

        // Get zone from request
        try {
            zone = snapshot.child(FirebaseNodes.TASK_ZONE).getValue(Zone.class);
            key = snapshot.child(FirebaseNodes.TASK_KEY).getValue(String.class);
        } catch (final DatabaseException e) {
            // TODO Test
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        if (zone == null || key == null) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        FirebaseDatabase.getInstance().getReference(FirebaseNodes.ENVIRONMENT_SQUADS)
        .child(String.valueOf(zone.getLatIndex())).child(String.valueOf(zone.getLonIndex())).child(key)
        .addListenerForSingleValueEvent(new HandledValueEventListener(userId, dataSnapshot -> {
            if (dataSnapshot.exists()) {
                action.accept(dataSnapshot.getValue(EMinion.class));
            } else {
                ResponseHandler.respond(userId, HttpCodes.NOT_FOUND);
            }
        }));
    }

    private void getPlayerMinions(final Consumer<List<PlayerMinion>> action) {
        // Get minion keys
        List<String> keys = new ArrayList<>();
        for (DataSnapshot child : snapshot.child(FirebaseNodes.TASK_MINIONS).getChildren()) {
            try {
                keys.add(child.getValue(String.class));
            } catch (DatabaseException e) {
                // TODO Test
                ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
                return;
            }
        }

        if (keys.size() < MIN_MINIONS || keys.size() > MAX_MINIONS) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        getPlayerMinionsHelper(keys, new ArrayList<>(), action);
    }

    private void getPlayerMinionsHelper(final List<String> keys, final List<PlayerMinion> minions, final Consumer<List<PlayerMinion>> action) {
        if (keys.isEmpty()) {
            action.accept(minions);
        }

        FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(userId)
        .child(FirebaseNodes.PLAYER_MINIONS).child(keys.get(0))
        .addListenerForSingleValueEvent(new HandledValueEventListener(userId, dataSnapshot -> {
            // Check if key from client is valid
            if (!dataSnapshot.exists()) {
                // TODO Test
                ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
                return;
            }

            // Remove key and add minion
            keys.remove(0);
            minions.add(dataSnapshot.getValue(PlayerMinion.class));

            getPlayerMinionsHelper(keys, minions, action);
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
