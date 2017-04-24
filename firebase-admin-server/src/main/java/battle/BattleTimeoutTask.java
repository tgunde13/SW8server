package battle;

import com.google.firebase.database.DataSnapshot;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import task.UnhandledValueEventListener;

import java.util.*;
import java.util.function.Consumer;

/**
 * After TIMEOUT milliseconds,
 * if the session has not yet advanced to next turn,
 * this will advance.
 */
class BattleTimeoutTask extends TimerTask {
    private static final int TIMEOUT = 30 * 1000; // 30 seconds in milliseconds

    private final int timeoutTurn;
    private final BattleSession session;
    private final Consumer<ChosenMoves> action;

    /**
     * Constructor.
     * @param timeoutTurn turn to check with after TIMEOUT milliseconds
     * @param session battle session that this belongs to
     */
    BattleTimeoutTask(final int timeoutTurn, final BattleSession session, final Consumer<ChosenMoves> action) {
        this.timeoutTurn = timeoutTurn;
        this.session = session;
        this.action = action;
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
        // If turns does not match now,
        // no need to fetch from Firebase,
        // We know that this is behind anyway
        if (timeoutTurn != session.getServerTurn()) {
            return;
        }

        // Get chosen moves from Firebase
        session.getChosenMovesRef().addListenerForSingleValueEvent(new UnhandledValueEventListener(snapshot -> {
            final ChosenMoves moves = snapshot.getValue(ChosenMoves.class);

            // Remove not chosen and skips
            for (final Map<String, String> playerMoves : moves.getMoves().values()) {
                playerMoves.values().removeAll(Collections.singleton(FirebaseValues.BATTLE_NOT_CHOSEN));
                playerMoves.values().removeAll(Collections.singleton(FirebaseValues.BATTLE_SKIP));
            }

            action.accept(moves);
        }));
    }
}
