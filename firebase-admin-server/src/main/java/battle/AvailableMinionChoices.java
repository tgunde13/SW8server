package battle;

import model.BattleAvatar;
import model.BattleState;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Tobias on 26/04/2017.
 */
public class AvailableMinionChoices extends PlayerChoices {
    private static final String CHOOSE_MINIONS_PREFIX = "minion-";

    public AvailableMinionChoices(final BattleState state, final int maxMinionsPerPlayer) {
        super(BattleSession.CHOOSE_MINIONS_TURN, getChooseMinionsMap(state, maxMinionsPerPlayer));
    }

    /**
     * Gets a map for choosing minions for some avatars.
     * @return the map
     */
    private static Map<String, Map<String, ChosenMove>> getChooseMinionsMap(final BattleState state, final int maxMinionsPerPlayer) {
        final Map<String, ChosenMove> chooseMinionsMap = new HashMap<>();
        for (int i = 0; i < maxMinionsPerPlayer; i++) {
            chooseMinionsMap.put(CHOOSE_MINIONS_PREFIX + String.valueOf(i), new AvailableMove());
        }

        final Map<String, Map<String, ChosenMove>> moves = new HashMap<>();

        Iterator<Map.Entry<String, BattleAvatar>> iterator = state.EntryIterator();
        while (iterator.hasNext()){
            Map.Entry<String, BattleAvatar> avatar = iterator.next();
            if (avatar.getValue().isPlayerControlled()) {
                moves.put(avatar.getKey(), chooseMinionsMap);
            }
        }
        return moves;
    }
}
