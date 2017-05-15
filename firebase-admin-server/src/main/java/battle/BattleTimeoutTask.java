package battle;

import firebase.DataChangeListenerAdapter;

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
    private final Consumer<FirebaseAvatarChoices> action;

    /**
     * Constructor.
     * @param timeoutTurn turn to check with after TIMEOUT milliseconds
     * @param session battle session that this belongs to
     * @param action action to call in order to advance
     */
    BattleTimeoutTask(final int timeoutTurn, final BattleSession session, final Consumer<FirebaseAvatarChoices> action) {
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
        System.out.println("TOB: BattleTimeoutTask, run");

        // If turns does not match now,
        // no need to fetch from Firebase,
        // We know that this is behind anyway
        if (timeoutTurn != session.getServerTurn()) {
            return;
        }

        // Get chosen moves from Firebase
        session.getChosenMovesRef().addListenerForSingleValueEvent(new DataChangeListenerAdapter(snapshot -> {
            final FirebaseAvatarChoices moves = snapshot.getValue(FirebaseAvatarChoices.class);

            // Remove not chosen moves
            for (final Map<String, ChosenMove> playerMoves: moves.getMoves().values()) {
                playerMoves.values().removeIf(ChosenMove::isAvailable);
            }

            action.accept(moves);
        }));
    }
}
