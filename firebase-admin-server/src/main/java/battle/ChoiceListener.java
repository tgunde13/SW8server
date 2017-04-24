package battle;

import com.google.firebase.database.DataSnapshot;
import firebase.DataChangeListener;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Listens to player choices.
 * Whenever a data change is accepted, an action is called.
 * A data change is accepted if all players have chosen a move for all their minions.
 */
class ChoiceListener extends DataChangeListener {
    private final Consumer<ChosenMoves> action;

    /**
     * Constructor
     * @param action action to call whenever a data change is accepted
     */
    ChoiceListener(final Consumer<ChosenMoves> action) {
        this.action = action;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        final ChosenMoves moves = dataSnapshot.getValue(ChosenMoves.class);

        for (final Map<String, String> playerMoves : moves.getMoves().values()) {
            // If a move is not chosen, abort
            if (playerMoves.containsValue(FirebaseValues.BATTLE_NOT_CHOSEN)) {
                return;
            }

            // Remove skips
            playerMoves.values().removeAll(Collections.singleton(FirebaseValues.BATTLE_SKIP));
        }

        action.accept(moves);
    }
}
