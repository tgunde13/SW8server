package battle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.GenericTypeIndicator;
import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;
import model.Minion;

import java.util.*;

/**
 * Models what moves are chosen by players,
 * and what turn those moves are chosen at
 */
public class FirebaseAvatarChoices extends AvatarChoices {
    private final int turn;

    /**
     * Constructs from values.
     * @param turn turn
     * @param moves moves
     */
    public FirebaseAvatarChoices(final int turn, final Map<String, Map<String, ChosenMove>> moves) {
        super(moves);
        this.turn = turn;
    }

    /**
     * Used by Firebase
     */
    @SuppressWarnings("unused")
    private FirebaseAvatarChoices() {
        super(new HashMap<>());
        turn = 0;
    }

    /**
     * Gets the turn.
     * Used by Firebase.
     * @return the turn
     */
    @SuppressWarnings("WeakerAccess")
    public int getTurn() {
        return turn;
    }
}
