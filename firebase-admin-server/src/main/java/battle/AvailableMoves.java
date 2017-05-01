package battle;

import model.BattleAvatar;
import model.BattleState;
import model.Minion;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Some choices that a player has when choosing moves.
 */
class AvailableMoves extends FirebaseAvatarChoices {
    /**
     * Constructor from values.
     * @param turn turn
     * @param state state to generate moves from
     */
    AvailableMoves(final int turn, final BattleState state) {
        super(turn, generateMoves(state));
    }

    /**
     * Generates moves from a state.
     * All moves will be BATTLE_NOT_CHOSEN.
     * @param state state to generate from
     * @return the moves
     */
    private static Map<String, Map<String, ChosenMove>> generateMoves(final BattleState state) {
        final Map<String, Map<String, ChosenMove>> moves = new HashMap<>();

        final Iterator<Map.Entry<String, BattleAvatar>> iterator = state.EntryIterator();
        while (iterator.hasNext()){
            final Map.Entry<String, BattleAvatar> avatar = iterator.next();
            if (avatar.getValue().isPlayerControlled()) {
                final Map<String, ChosenMove> avatarChoices = new HashMap<>();

                for (final Map.Entry<String, Minion> minionEntry : avatar.getValue().getBattleMinions().entrySet()) {
                    if (minionEntry.getValue().getBattleStats().isAlive()) {
                        avatarChoices.put(minionEntry.getKey(), new AvailableMove());
                    }
                }

                moves.put(avatar.getKey(), avatarChoices);
            }
        }

        return moves;
    }
}
