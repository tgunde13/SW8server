package battle;

import com.google.firebase.database.Exclude;
import model.BattleMinionIdentifier;

/**
 * Created by Tobias on 26/04/2017.
 */
public class ChosenMove extends BattleMinionIdentifier {
    private final Boolean available;

    public ChosenMove(final String avatarKey, final String minionKey, final Boolean available) {
        super(avatarKey, minionKey);
        this.available = available;
    }

    @SuppressWarnings("unused")
    private ChosenMove() {
        available = null;
    }


    public Boolean getAvailable() {
        return available;
    }

    @Exclude
    boolean isAvailable() {
        return available != null;
    }
}
