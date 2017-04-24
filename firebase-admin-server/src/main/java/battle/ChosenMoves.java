package battle;

import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;
import model.Minion;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Models what moves are chosen by players,
 * and what turn those moves are chosen at
 */
public class ChosenMoves {
    private final int turn;
    private final Map<String, Map<String, String>> moves;

    /**
     * Constructs from values.
     * @param turn turn
     * @param moves moves
     */
    ChosenMoves(final int turn, final Map<String, Map<String, String>> moves) {
        this.turn = turn;
        this.moves = moves;
    }

    /**
     * Constructs from a state.
     * All moves will be BATTLE_NOT_CHOSEN.
     * @param turn turn
     * @param state state
     */
    ChosenMoves(final int turn, final BattleState state) {
        this.turn = turn;

        moves = new HashMap<>();

        // Add options to choose moves
        addChooseMovesMap(state.getTeamOne());
        addChooseMovesMap(state.getTeamTwo());
    }

    /**
     * Adds options to choose moves from some avatars.
     * @param avatars The avatars to make options from
     */
    private void addChooseMovesMap(final List<BattleAvatar> avatars) {
        for (final BattleAvatar avatar : avatars) {
            if (avatar.isPlayerControlled()) {
                final Map<String, String> avatarMap = new HashMap<>();

                for (final Map.Entry<String, Minion> minionEntry : avatar.getBattleMinions().entrySet()) {
                    if (minionEntry.getValue().getBattleStats().isAlive()) {
                        avatarMap.put(minionEntry.getKey(), FirebaseValues.BATTLE_NOT_CHOSEN);
                    }
                }

                moves.put(avatar.getUserId(), avatarMap);
            }
        }
    }

    /**
     * Used by Firebase
     */
    @SuppressWarnings("unused")
    private ChosenMoves() {
        turn = 0;
        moves = null;
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
    public Map<String, Map<String, String>> getMoves() {
        return moves;
    }
}
