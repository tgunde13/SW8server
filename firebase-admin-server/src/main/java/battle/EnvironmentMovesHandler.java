package battle;

import model.BattleAvatar;
import model.BattleMinionIdentifier;
import model.BattleState;
import model.Minion;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Handler for generating and adding environment moves
 */
class EnvironmentMovesHandler {
    /**
     * Generate and add moves for environment minions to some avatar moves.
     * Each environment minion chooses a random moves
     * @param state the state to generate moves from
     * @param choices the moves to add into
     */
    static void addMoves(final BattleState state, final AvatarChoices choices) {
        final Random random = new Random();

        final Iterator<Map.Entry<String, BattleAvatar>> iterator = state.EntryIterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, BattleAvatar> avatarEntry = iterator.next();
            final String avatarKey = avatarEntry.getKey();
            final BattleAvatar avatar = avatarEntry.getValue();
            if (!avatar.isPlayerControlled()) {
                for (final Map.Entry<String, Minion> minionEntry : avatar.getBattleMinions().entrySet()) {
                    final String minionKey = minionEntry.getKey();

                    // Get available moves
                    final List<BattleMinionIdentifier> availableMoves = minionEntry.getValue().getTypeClass()
                            .getAvailableMoves(state, new BattleMinionIdentifier(avatarKey, minionKey));

                    // Choose randomly
                    final BattleMinionIdentifier defender = availableMoves.get(random.nextInt(availableMoves.size()));

                    choices.put(avatarKey, minionKey, new ChosenMove(defender.getAvatarKey(), defender.getMinionKey(), null));
                }
            }
        }
    }
}
