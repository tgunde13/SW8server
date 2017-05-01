package battle;

import com.google.firebase.database.DataSnapshot;
import firebase.DataChangeListener;

import java.util.Map;
import java.util.function.Consumer;

/**
 * Listens to player choices.
 * Whenever a data change is accepted, an action is called.
 * A data change is accepted if all players have chosen a move for all their minions.
 */
public class ChoiceListener extends DataChangeListener {
    private final Consumer<FirebaseAvatarChoices> action;

    /**
     * Constructor
     * @param action action to call whenever a data change is accepted
     */
    public ChoiceListener(final Consumer<FirebaseAvatarChoices> action) {
        this.action = action;
    }

    @Override
    public void onDataChange(final DataSnapshot dataSnapshot) {
        System.out.println("TOB: ChoiceListener, run");

        final FirebaseAvatarChoices choices = dataSnapshot.getValue(FirebaseAvatarChoices.class);

        // If a move is not chosen, abort
        for (final Map<String, ChosenMove> playerMoves: choices.getMoves().values()) {
            for (final ChosenMove move : playerMoves.values()) {
                if (move.isAvailable()) {
                    return;
                }
            }
        }

        action.accept(choices);
    }
}
