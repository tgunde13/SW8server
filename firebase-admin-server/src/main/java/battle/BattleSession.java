package battle;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import firebase.DataChangeListenerAdapter;
import firebase.FirebaseNodes;
import firebase.FirebaseValues;
import model.BattleState;
import model.PlayerMinion;

import java.util.*;

/**
 * Manages a battle.
 */
public class BattleSession {
    static final int CHOOSE_MINIONS_TURN = 0;

    private final BattleState state;
    private int maxMinionsPerPlayer;
    private final DatabaseReference ref, chosenMovesRef;

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
        chosenMovesRef.setValue(new AvailableMinionChoices(state, maxMinionsPerPlayer));

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
    private void tryAdvanceChooseMinions(final PlayerChoices moves) {
        System.out.println("TOB: BattleSession, tryAdvanceChooseMinions");
        // Advance to next turn with a lock
        final boolean succeeded = tryAdvanceTurn(moves.getTurn());
        if (!succeeded) {
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
    private void tryAdvance(final PlayerChoices moves) {
        // Advance to next turn with a lock
        final boolean succeeded = tryAdvanceTurn(moves.getTurn());
        if (!succeeded) {
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

        chosenMovesRef.setValue(new AvailableMoves(serverTurn, state)).addOnSuccessListener(aVoid -> {
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

    /**
     * Starts choosing minions from player choices.
     * When done, an action is called.
     * @param allPlayersChoices player choices
     * @param action action to call
     */
    private void chooseMinions(final Map<String, Map<String, ChosenMove>> allPlayersChoices, final Runnable action) {
        System.out.println("TOB: BattleSession, chooseMinions");

        // Count moves
        int count = 0;
        for (final Map<String, ChosenMove> aPlayerChoices : allPlayersChoices.values()) {
            count += aPlayerChoices.size();
        }

        final SynchronizedCountdown countdown = new SynchronizedCountdown(count, action);

        // Go through the players involved
        for (final Map.Entry<String, Map<String, ChosenMove>> playerChoices : allPlayersChoices.entrySet()) {
            final String playerKey = playerChoices.getKey();

            // If player has duplicate minions (tries to cheat), skip
            final Collection<ChosenMove> playerMoves = playerChoices.getValue().values();
            if (containDuplicateMinionKeys(playerMoves)) {
                // TODO Test
                System.out.println("TOB: BattleSession, containDuplicateMinionKeys(playerMoves)");
                countdown.step(playerMoves.size());
                continue;
            }

            // Go through a player's chosen minions
            for (final ChosenMove move : playerMoves) {
                FirebaseDatabase.getInstance().getReference(FirebaseNodes.PLAYERS).child(playerKey)
                        .child(FirebaseNodes.PLAYER_MINIONS).child(move.getMinionKey())
                        .addListenerForSingleValueEvent(new DataChangeListenerAdapter(dataSnapshot -> {
                            // If minion was not found, ignore
                            if (!dataSnapshot.exists()) {
                                // TODO Test
                                System.out.println("TOB: BattleSession, chooseMinions, !dataSnapshot.exists(), minionKey: " + move.getMinionKey());
                                countdown.step();
                                return;
                            }

                            // Add to state
                            final PlayerMinion minion = dataSnapshot.getValue(PlayerMinion.class);
                            minion.createBattleStats();
                            state.addSynchronized(playerKey, move.getMinionKey(), minion);
                            countdown.step();
                        }));
            }
        }
    }

    /**
     * Gets if a collection of moves contains duplicate minion keys.
     * @param moves the collection to check
     * @return true if, and only if, the collection contains duplicate minion keys
     */
    private static boolean containDuplicateMinionKeys(final Collection<ChosenMove> moves) {
        final Set<String> keys = new HashSet<>();

        for (final ChosenMove move : moves) {
            // If keys already contained the new key,
            // return true, since duplicate was found
            if (!keys.add(move.getMinionKey())) {
                return true;
            }
        }

        return false;
    }
}
