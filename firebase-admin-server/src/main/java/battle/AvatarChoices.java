package battle;

import model.Minion;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Tobias on 28/04/2017.
 */
public class AvatarChoices {
    private final Map<String, Map<String, ChosenMove>> moves;

    /**
     * Constructs from values.
     * @param moves moves
     */
    public AvatarChoices(final Map<String, Map<String, ChosenMove>> moves) {
        this.moves = moves;
    }

    public void put(final String avatarKey, final String minionKey, final ChosenMove move) {
        // If avatar exists
        final Map<String, ChosenMove> existingAvatarMap = moves.get(avatarKey);
        if (existingAvatarMap != null) {
            // Add to existing map
            existingAvatarMap.put(minionKey, move);
            return;
        }

        // Create a new map and add that
        final Map<String, ChosenMove> newMoveMap = new HashMap<>();
        newMoveMap.put(minionKey, move);
        moves.put(avatarKey, newMoveMap);
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
