package battle;

import com.google.firebase.database.Exclude;
import model.BattleMinionIdentifier;

/**
 * Identifies a battle minion to attack,
 * but also contains if the move is available.
 * The available field is null if, and only if, this is not available.
 */
public class ChosenMove extends BattleMinionIdentifier {
    private final Boolean available;

    /**
     * Constructor
     * @param avatarKey avatar key
     * @param minionKey minion key
     * @param available whether the move is available or not
     */
    public ChosenMove(final String avatarKey, final String minionKey, final Boolean available) {
        super(avatarKey, minionKey);
        this.available = available;
    }

    /**
     * Constructor used by Firebase.
     */
    @SuppressWarnings("unused")
    private ChosenMove() {
        available = null;
    }

    /**
     * Gets the available field.
     * Used by Firebase.
     * @return the available field
     */
    @SuppressWarnings("unused")
    public Boolean getAvailable() {
        return available;
    }

    /**
     * Gets if this is available.
     * @return true if, and only if, this is available
     */
    @Exclude
    boolean isAvailable() {
        return available != null;
    }
}
