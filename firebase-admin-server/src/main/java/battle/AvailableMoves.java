package battle;

import model.BattleAvatar;
import model.BattleState;
import model.Minion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobias on 25/04/2017.
 */
public class AvailableMoves extends PlayerChoices {
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

        for (final BattleAvatar avatar : state) {
            if (avatar.isPlayerControlled()) {
                final Map<String, ChosenMove> avatarChoices = new HashMap<>();

                for (final Map.Entry<String, Minion> minionEntry : avatar.getBattleMinions().entrySet()) {
                    if (minionEntry.getValue().getBattleStats().isAlive()) {
                        avatarChoices.put(minionEntry.getKey(), new AvailableMove());
                    }
                }

                moves.put(avatar.getUserId(), avatarChoices);
            }
        }

        return moves;
    }
}
