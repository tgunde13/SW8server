package battle;

import model.BattleAvatar;
import model.BattleState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * The choices that a player can choose from when choosing minions.
 */
class AvailableMinionChoices extends FirebaseAvatarChoices {
    private static final String CHOOSE_MINIONS_PREFIX = "minion-";

    /**
     * Constructor
     * @param state state to constructor from
     * @param maxMinionsPerPlayer maximum number of minions a player can choose
     */
    AvailableMinionChoices(final BattleState state, final int maxMinionsPerPlayer) {
        super(BattleSession.CHOOSE_MINIONS_TURN, getChooseMinionsMap(state, maxMinionsPerPlayer));
    }

    /**
     * Gets a map for choosing minions for some avatars.
     * @param state state to construct map from
     * @param maxMinionsPerPlayer maximum number of minions a player can choose
     * @return the map
     */
    private static Map<String, Map<String, ChosenMove>> getChooseMinionsMap(final BattleState state, final int maxMinionsPerPlayer) {
        final Map<String, ChosenMove> chooseMinionsMap = new HashMap<>();
        for (int i = 0; i < maxMinionsPerPlayer; i++) {
            chooseMinionsMap.put(CHOOSE_MINIONS_PREFIX + String.valueOf(i), new AvailableMove());
        }

        final Map<String, Map<String, ChosenMove>> moves = new HashMap<>();

        final Iterator<Map.Entry<String, BattleAvatar>> iterator = state.EntryIterator();
        while (iterator.hasNext()){
            final Map.Entry<String, BattleAvatar> avatar = iterator.next();
            if (avatar.getValue().isPlayerControlled()) {
                moves.put(avatar.getKey(), chooseMinionsMap);
            }
        }
        return moves;
    }
}
