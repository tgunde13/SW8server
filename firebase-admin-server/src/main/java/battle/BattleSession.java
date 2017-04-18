package battle;

import com.google.firebase.database.FirebaseDatabase;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleState;

/**
 * Created by Tobias on 18/04/2017.
 */
public class BattleSession {
    private final BattleState state;

    public BattleSession(BattleState state) {
        this.state = state;
    }

    public void start() {




        //FirebaseDatabase.getInstance().getReference(FirebaseNodes.BATTLES).push()
        //.child(FirebaseNodes.BATTLE_DESIRED_MOVES).child(userId).setValue(FirebaseValues.BATTLE_NOT_CHOSEN);
    }
}
