package battle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;

import java.util.List;

/**
 * Created by Tobias on 18/04/2017.
 */
public class BattleSession {
    private final BattleState state;
    private final DatabaseReference ref;

    public BattleSession(BattleState state) {
        this.state = state;
        ref = FirebaseDatabase.getInstance().getReference(FirebaseNodes.BATTLES).push();
    }

    public void start() {
        setChooseMinions(state.getTeamOne());
        setChooseMinions(state.getTeamTwo());
    }

    private void setChooseMinions(final List<BattleAvatar> avatars) {
        for (final BattleAvatar avatar : avatars) {
            if (avatar.isPlayerControlled()) {
                ref.child(FirebaseNodes.BATTLE_DESIRED_MOVES).child(avatar.getUserId()).setValue(FirebaseValues.BATTLE_NOT_CHOSEN);
            }
        }
    }

    public String getKey() {
        return ref.getKey();
    }
}
