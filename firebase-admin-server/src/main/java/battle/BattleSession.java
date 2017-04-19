package battle;

import com.google.firebase.database.*;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;
import model.PlayerMinion;
import task.*;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

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
        // Setup battle on Firebase
        setChooseMinions(state.getTeamOne());
        setChooseMinions(state.getTeamTwo());
        ref.child(FirebaseNodes.BATTLE_STATE).setValue(state);

        // Listen for chooses of minions from players
        ref.child(FirebaseNodes.BATTLE_DESIRED_MOVES).addValueEventListener(new UnhandledValueEventListener(dataSnapshot -> {

        }));
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

    /*private void getPlayerMinions(final Consumer<List<PlayerMinion>> action) {
        // Get minion keys
        final List<String> keys = new ArrayList<>();
        for (final DataSnapshot child : snapshot.child(FirebaseNodes.TASK_MINIONS).getChildren()) {
            try {
                keys.add(child.getValue(String.class));
            } catch (final DatabaseException e) {
                // TODO Test
                ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
                return;
            }
        }

        if (keys.size() < MIN_MINIONS || keys.size() > MAX_MINIONS) {
            ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
            return;
        }

        getPlayerMinionsHelper(keys, new ArrayList<>(), action);
    }

    private void getPlayerMinionsHelper(final List<String> keys, final List<PlayerMinion> minions, final Consumer<List<PlayerMinion>> action) {
        if (keys.isEmpty()) {
            action.accept(minions);
        }

        FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(userId)
        .child(FirebaseNodes.PLAYER_MINIONS).child(keys.get(0))
        .addListenerForSingleValueEvent(new HandledValueEventListener(userId, dataSnapshot -> {
            // Check if key from client is valid
            if (!dataSnapshot.exists()) {
                // TODO Test
                ResponseHandler.respond(userId, HttpCodes.BAD_REQUEST);
                return;
            }

            // Remove key and add minion
            keys.remove(0);
            minions.add(dataSnapshot.getValue(PlayerMinion.class));

            getPlayerMinionsHelper(keys, minions, action);
        }));
    }*/
}
