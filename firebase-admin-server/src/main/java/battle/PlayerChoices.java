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
public class PlayerChoices {
    private final int turn;
    private final Map<String, Map<String, ChosenMove>> moves;

    /**
     * Constructs from values.
     * @param turn turn
     * @param moves moves
     */
    public PlayerChoices(final int turn, final Map<String, Map<String, ChosenMove>> moves) {
        this.turn = turn;
        this.moves = moves;
    }

    /**
     * Used by Firebase
     */
    @SuppressWarnings("unused")
    private PlayerChoices() {
        turn = 0;
        moves = new HashMap<>();
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

    /**
     * Gets the moves.
     * Used by Firebase.
     * @return the moves
     */
    @SuppressWarnings("WeakerAccess")
    public Map<String, Map<String, ChosenMove>> getMoves() {
        return moves;
    }
}
