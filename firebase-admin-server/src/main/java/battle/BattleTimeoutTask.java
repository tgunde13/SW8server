package battle;

import com.google.firebase.database.DataSnapshot;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import task.UnhandledValueEventListener;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

/**
 * After TIMEOUT milliseconds,
 * if the session has not yet advanced to next turn,
 * this will advance.
 */
class BattleTimeoutTask extends TimerTask {
    private static final int TIMEOUT = 30 * 1000; // 30 seconds in milliseconds

    private final int timeoutTurn;
    private final BattleSession session;

    /**
     * Constructor.
     * @param timeoutTurn turn to check with after TIMEOUT milliseconds
     * @param session battle session that this belongs to
     */
    BattleTimeoutTask(final int timeoutTurn, final BattleSession session) {
        this.timeoutTurn = timeoutTurn;
        this.session = session;
    }

    /**
     * Schedule this to run after TIMEOUT milliseconds.
     */
    void schedule() {
        final Timer timer = new Timer(true);
        timer.schedule(this, TIMEOUT);
    }

    @Override
    public void run() {
        // If turns does not match now, return
        if (timeoutTurn != session.getServerTurn()) {
            return;
        }

        // Get chosen moves from Firebase
        session.getChosenMovesRef().addListenerForSingleValueEvent(new UnhandledValueEventListener(snapshot -> {

            // Get chosen moves
            final Map<String, String> chosenMoves = new HashMap<>();
            for (final DataSnapshot playerSnapshot : snapshot.child(FirebaseNodes.BATTLE_PLAYERS).getChildren()) {
                for (final DataSnapshot keySnapshot : playerSnapshot.getChildren()) {
                    final String key = keySnapshot.getValue(String.class);

                    // If a player has not yet chosen, skip that move
                    if (!key.equals(FirebaseValues.BATTLE_NOT_CHOSEN)) {
                        chosenMoves.put(playerSnapshot.getKey(), key);
                    }
                }
            }

            // Advance to next turn with a lock
            if (!session.tryAdvanceTurn(timeoutTurn)) {
                return;
            }

            session.computeAndUpdateNextTurn(chosenMoves);
        }));
    }
}
