package battle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;
import task.UnhandledValueEventListener;

import java.util.*;

/**
 * Manages a battle.
 */
public class BattleSession {
    private static final int CHOOSE_MINIONS_TURN = 0;
    private static final int MIN_MINIONS_PER_PLAYER = 1;

    private final BattleState state;
    private final DatabaseReference ref, chosenMovesRef;

    private final int maxMinionsPerPlayer;
    private final Map<String, Object> chooseMinionsMap;

    private final ValueEventListener listener;

    private int serverTurn;

    /**
     * Constructor
     * @param state starting state of the battle, containing environment squads and players
     * @param maxMinionsPerPlayer maximum number of minions each player can choose
     */
    public BattleSession(final BattleState state, final int maxMinionsPerPlayer) {
        this.state = state;
        this.maxMinionsPerPlayer = maxMinionsPerPlayer;

        chooseMinionsMap = new HashMap<>();
        for (int i = 0; i < maxMinionsPerPlayer; i++) {
            chooseMinionsMap.put(String.valueOf(i), FirebaseValues.BATTLE_NOT_CHOSEN);
        }

        ref = FirebaseDatabase.getInstance().getReference(FirebaseNodes.BATTLES).push();
        chosenMovesRef = ref.child(FirebaseNodes.BATTLE_CHOSEN_MOVES);

        listener = getChoiceListener();

        serverTurn = CHOOSE_MINIONS_TURN;
    }

    /**
     * Starts the battle.
     * During the first turn (turn 0),
     * the players are to choose minions.
     */
    public void start() {
        // Upload state
        ref.child(FirebaseNodes.BATTLE_STATE).setValue(state);

        // Upload map for choosing minions
        chosenMovesRef.setValue(getChooseMinionsMap());

        // Listen to player choices
        chosenMovesRef.addValueEventListener(listener);

        // Start a timeout task
        new BattleTimeoutTask(CHOOSE_MINIONS_TURN, this).schedule();
    }

    /**
     * Gets a map for choosing minions to upload to Firebase.
     * @return the map
     */
    private Map<String, Object> getChooseMinionsMap() {
        final Map<String, Object> chosenMoves = new HashMap<>();

        // Add options to choose minions
        final Map<String, Object> chosenMovesPlayers = new HashMap<>();
        addChooseMinions(chosenMovesPlayers, state.getTeamOne());
        addChooseMinions(chosenMovesPlayers, state.getTeamTwo());
        chosenMoves.put(FirebaseNodes.BATTLE_PLAYERS, chosenMovesPlayers);

        // Add turn
        chosenMoves.put(FirebaseNodes.BATTLE_TURN, CHOOSE_MINIONS_TURN);

        return chosenMoves;
    }

    /**
     * Adds objects to a map for choosing minions.
     * @param desiredMoves the map to add to
     * @param avatars the avatars to add from
     */
    private void addChooseMinions(final Map<String, Object> desiredMoves, final List<BattleAvatar> avatars) {
        for (final BattleAvatar avatar : avatars) {
            if (avatar.isPlayerControlled()) {
                desiredMoves.put(avatar.getUserId(), chooseMinionsMap);
            }
        }
    }

    /**
     * Gets the Firebase key of this.
     * @return the key
     */
    public String getKey() {
        return ref.getKey();
    }

    /**
     * Gets the current server turn.
     * @return the turn
     */
    int getServerTurn() {
        return serverTurn;
    }

    /**
     * Gets the Firebase reference of the node to choose minions.
     * @return the reference
     */
    DatabaseReference getChosenMovesRef() {
        return chosenMovesRef;
    }

    /**
     * Gets listener for player choices.
     * @return the listener
     */
    private ValueEventListener getChoiceListener() {
        // Listen to choices
        return new UnhandledValueEventListener(dataSnapshot -> {
            final int firebaseTurn = dataSnapshot.child(FirebaseNodes.BATTLE_TURN).getValue(Integer.class);

            // If turns does not match now, return
            if (firebaseTurn != serverTurn) {
                return;
            }

            // Get chosen moves
            final Map<String, String> chosenMoves = new HashMap<>();
            for (final DataSnapshot playerSnapshot : dataSnapshot.child(FirebaseNodes.BATTLE_PLAYERS).getChildren()) {
                for (final DataSnapshot keySnapshot : playerSnapshot.getChildren()) {
                    final String key = keySnapshot.getValue(String.class);

                    // If a player has not yet chosen, return
                    if (key.equals(FirebaseValues.BATTLE_NOT_CHOSEN)) {
                        return;
                    }

                    chosenMoves.put(playerSnapshot.getKey(), key);
                }
            }

            // Advance to next turn with a lock
            if (!tryAdvanceTurn(firebaseTurn)) {
                return;
            }

            computeAndUpdateNextTurn(chosenMoves);
        });
    }

    /**
     * If the test turn matches the server turn,
     * this method advances to the next turn.
     * The advancement is done synchronized to avoid race conditions
     * @param testTurn turn to test with
     * @return true if, and only if, the turns matched
     */
    synchronized boolean tryAdvanceTurn(final int testTurn) {
        if (serverTurn != testTurn) {
            return false;
        }

        serverTurn++;
        return true;
    }

    /**
     * Computes the next turn.
     * Updates Firebase.
     * Resets chosen moves on Firebase.
     * Schedules a new timeout.
     * @param chosenMoves moves to compute with.
     */
    void computeAndUpdateNextTurn(final Map<String, String> chosenMoves) {
        // TODO
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
