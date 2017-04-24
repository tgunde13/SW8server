package battle;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import firebase.DataChangeListener;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleAvatar;
import model.BattleState;
import model.Player;
import model.PlayerMinion;
import task.UnhandledValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Manages a battle.
 */
public class BattleSession {
    private static final int CHOOSE_MINIONS_TURN = 0;
    private static final int MIN_MINIONS_PER_PLAYER = 1;
    private static final String CHOOSE_MINIONS_PREFIX = "player-";

    private final BattleState state;
    private final DatabaseReference ref, chosenMovesRef;

    private final int maxMinionsPerPlayer;
    private final Map<String, String> chooseMinionsMap;

    private ChoiceListener listener;

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
            chooseMinionsMap.put(CHOOSE_MINIONS_PREFIX + String.valueOf(i), FirebaseValues.BATTLE_NOT_CHOSEN);
        }

        ref = FirebaseDatabase.getInstance().getReference(FirebaseNodes.BATTLES).push();
        chosenMovesRef = ref.child(FirebaseNodes.BATTLE_CHOSEN_MOVES);

        serverTurn = CHOOSE_MINIONS_TURN;
    }

    /**
     * Starts the battle.
     * During the first turn (turn 0),
     * the players are to choose minions.
     */
    public void start() {
        uploadState();

        // Upload map for choosing minions
        final Map<String, Map<String, String>> moves = new HashMap<>();
        moves.putAll(getChooseMinionsMap(state.getTeamOne()));
        moves.putAll(getChooseMinionsMap(state.getTeamTwo()));
        chosenMovesRef.setValue(new ChosenMoves(CHOOSE_MINIONS_TURN, moves));

        // Listen to player choices
        listener = new ChoiceListener(this::tryAdvanceChooseMinions);
        chosenMovesRef.addValueEventListener(listener);

        // Start a timeout task
        new BattleTimeoutTask(CHOOSE_MINIONS_TURN, this, this::tryAdvanceChooseMinions).schedule();
    }

    /**
     * Uploads state to Firebase
     */
    private void uploadState() {
        ref.child(FirebaseNodes.BATTLE_STATE).setValue(state);
    }

    /**
     * Gets a map for choosing minions for some avatars.
     * @param avatars The avatars to map with
     * @return the map
     */
    private Map<String, Map<String, String>> getChooseMinionsMap(final List<BattleAvatar> avatars) {
        final Map<String, Map<String, String>> result = new HashMap<>();

        for (final BattleAvatar avatar : avatars) {
            if (avatar.isPlayerControlled()) {
                result.put(avatar.getUserId(), chooseMinionsMap);
            }
        }

        return result;
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
     * If the turn of the chosen moves matches the server turn,
     * this method advances to turn 1
     * (where players choose moves for their minions).
     * This method also updates Firebase and schedules a timeout.
     * @param moves chosen moves
     */
    private void tryAdvanceChooseMinions(final ChosenMoves moves) {
        System.out.println("TOB: BattleSession, tryAdvanceChooseMinions");
        // Advance to next turn with a lock
        if (!tryAdvanceTurn(moves.getTurn())) {
            return;
        }

        // Switch listener to perform minion moves from now one
        chosenMovesRef.removeEventListener(listener);
        listener = new ChoiceListener(this::tryAdvance);
        chosenMovesRef.addValueEventListener(listener);

        chooseMinions(moves.getMoves(), this::updateFirebaseAndSchedule);
    }

    /**
     * If the turn of the chosen moves matches the server turn,
     * this method advance to the next turn.
     * This method also updates Firebase and schedules a timeout.
     * @param moves chosen moves
     */
    private void tryAdvance(final ChosenMoves moves) {
        // Advance to next turn with a lock
        if (!tryAdvanceTurn(moves.getTurn())) {
            return;
        }

        state.advance(moves.getMoves());

        updateFirebaseAndSchedule();
    }

    /**
     * If the test turn matches the server turn,
     * this method advances to the next turn.
     * The advancement is done synchronized to avoid race conditions
     * @param testTurn turn to test with
     * @return true if, and only if, the turns matched
     */
    private synchronized boolean tryAdvanceTurn(final int testTurn) {
        if (serverTurn != testTurn) {
            System.out.println("TOB: BattleSession, tryAdvanceTurn, false");
            return false;
        }

        serverTurn++;
        System.out.println("TOB: BattleSession, tryAdvanceTurn, true");
        return true;
    }

    /**
     * Updates state on Firebase.
     * Resets chosen moves on Firebase.
     * Schedules a new timeout.
     */
    private void updateFirebaseAndSchedule() {
        System.out.println("TOB: BattleSession, updateFirebaseAndSchedule");
        uploadState();

        chosenMovesRef.setValue(new ChosenMoves(serverTurn, state)).addOnSuccessListener(aVoid -> {
            // If if game is over
            if (state.isOver()) {
                // Stop listening
                chosenMovesRef.removeEventListener(listener);
                System.out.println("TOB: BattleSession, updateFirebaseAndSchedule, game is over");
                return;
            }

            // Schedule timeout after resetting to ensure minimum timeout
            new BattleTimeoutTask(serverTurn, this, this::tryAdvance).schedule();
        });
    }

    private void chooseMinions(final Map<String, Map<String, String>> allPlayersChoices, final Runnable action) {
        System.out.println("TOB: BattleSession, chooseMinions");

        // Count moves
        int count = 0;
        for (final Map<String, String> aPlayerChoices : allPlayersChoices.values()) {
            count += aPlayerChoices.size();
        }

        final SynchronizedCountdown countdown = new SynchronizedCountdown(count, action);

        for (final Map.Entry<String, Map<String, String>> playerChoices : allPlayersChoices.entrySet()) {
            final String playerKey = playerChoices.getKey();

            for (final String minionKey : playerChoices.getValue().values()) {
                // If player chose to skip or has not chosen, continue
                if (minionKey.equals(FirebaseValues.BATTLE_SKIP) || minionKey.equals(FirebaseValues.BATTLE_NOT_CHOSEN)) {
                    countdown.step();
                    continue;
                }

                FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(playerKey)
                        .child(FirebaseNodes.PLAYER_MINIONS).child(minionKey)
                        .addListenerForSingleValueEvent(new UnhandledValueEventListener(dataSnapshot -> {
                            // If minion was not found, ignore
                            if (!dataSnapshot.exists()) {
                                System.out.println("TOB: BattleSession, chooseMinions, !dataSnapshot.exists(), minionKey: " + minionKey);
                                countdown.step();
                                return;
                            }

                            final PlayerMinion minion = dataSnapshot.getValue(PlayerMinion.class);
                            minion.createBattleStats();
                            state.addSynchronized(playerKey, minionKey, minion);
                            countdown.step();
                        }));
            }
        }
    }
}
