package model;

import battle.AvatarChoices;
import battle.ChosenMove;

import java.util.*;

/**
 * A class that represents a state of a battle, includes the teams participating in the battle,
 * the moves performed to get to this state and a status of the battle
 */
public class BattleState {
    public static final String TEAM_TWO_WIN = "teamTwoWin";
    public static final String TEAM_ONE_WIN = "teamOneWin";
    private static final String RUNNING  = "running";
    private Map<String, BattleAvatar> teamOne;
    private Map<String, BattleAvatar> teamTwo;
    private List<BattleMove> moves;
    private String status;

    /**
     * Constuctor of a battlestate, used when starting a battle, as no moves have been performed to start a battle
     * the moves are not part of the constructor
     * @param teamOne is the first team of the battle, should never consist of other than players
     * @param teamTwo is the second team of the battle, it can consist of either a team of EMinions or a team of players
     */
    public BattleState(final Map<String, BattleAvatar> teamOne, final Map<String, BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        moves = new ArrayList<>();
        status = RUNNING;
    }

    /**
     * Default constuctor, for Firebase
     */
    private BattleState() {
    }

    /**
     * Getter for teamOne
     * @return a map of BattleAvatar's on team one
     */
    public Map<String, BattleAvatar> getTeamOne() { return teamOne; }

    /**
     * Getter for teamTwo
     * @return a map of BattleAvatar's on team two
     */
    public Map<String, BattleAvatar> getTeamTwo() { return teamTwo; }

    /**
     * Getter for moves
     * @return a list of BattleMove's
     */
    public List<BattleMove> getMoves() { return moves; }


    /**
     * Getter for the status
     * @return the status of the state
     */
    public String getStatus(){ return status; }

    /**
     * Gets if the battle is over
     * @return true if, and only if, the battle is over
     */
    public boolean isOver(){
        return status.equals(TEAM_ONE_WIN) || status.equals(TEAM_TWO_WIN);
    }

    /**
     * Evaluates the status of the battle to determine if teamOne or teamTwo has won or if the battle is still ongoing
     */
    public void evaluateStatus() {

        boolean teamOneDead = true;
        boolean teamTwoDead = true;

        if(teamOne.values().isEmpty()){
            status = TEAM_TWO_WIN;
        } else if(teamTwo.values().isEmpty()){
            status = TEAM_ONE_WIN;
        }

        for(final BattleAvatar avatar: teamOne.values()){
            if(avatar.hasAliveMinions()){
                teamOneDead = false;
                break;
            }
        }

        for(final BattleAvatar avatar: teamTwo.values()){
            if(avatar.hasAliveMinions()){
                teamTwoDead = false;
                break;
            }
        }
        if(teamOneDead){
            status = TEAM_TWO_WIN;
        } else if(teamTwoDead) {
            status = TEAM_ONE_WIN;
        }
    }

    /**
     * Advances to the next turn by performing moves.
     * @param chosenMoves the moves to perform
     */
    public void advance(final Map<String, Map<String, ChosenMove>> chosenMoves) {
        moves.clear();
        advanceHelper(chosenMoves);
    }

    /**
     * Performs some moves en a prioritised way.
     * @param chosenMoves the moves to perform
     */
    private void advanceHelper(final Map<String, Map<String, ChosenMove>> chosenMoves) {
        final AvatarChoices priorityMoves = getHighestPriorityMoves(chosenMoves);

        if (priorityMoves.getMoves().isEmpty()) {
            return;
        }

        // Perform the moves
        for (final Map.Entry<String, Map<String, ChosenMove>> moveEntries : priorityMoves.getMoves().entrySet()) {
            final String attackerAvatarKey = moveEntries.getKey();

            for (final Map.Entry<String, ChosenMove> playerEntries : moveEntries.getValue().entrySet()) {
                final String attackerMinionKey = playerEntries.getKey();

                performAndReportMove(new BattleMinionIdentifier(attackerAvatarKey, attackerMinionKey), playerEntries.getValue());

                // Remove move
                chosenMoves.get(attackerAvatarKey).remove(attackerMinionKey);
            }
        }

        advanceHelper(chosenMoves);
    }

    /**
     * Get the moves with highest priority.
     * Minions with higher speed performs moves first.
     * Minions with same speed, but higher level, performs moves first.
     * @param moves moves to prioritise
     * @return the moves with highest priority
     */
    private AvatarChoices getHighestPriorityMoves(final Map<String, Map<String, ChosenMove>> moves) {
        int speed = 0, level = 0;
        final AvatarChoices fastestMoves = new AvatarChoices(new HashMap<>());

        for (final Map.Entry<String, Map<String, ChosenMove>> playerMoves : moves.entrySet()) {
            for (final Map.Entry<String, ChosenMove> minionMove : playerMoves.getValue().entrySet()) {
                final Minion minion = getAvatar(playerMoves.getKey()).getBattleMinions().get(minionMove.getKey());

                if (minion.getBattleStats().getCurrentSpeed() > speed) {
                    // If faster than the previously fastest,
                    // Clear, add, and update speed and level
                    fastestMoves.getMoves().clear();
                    fastestMoves.put(playerMoves.getKey(), minionMove.getKey(), minionMove.getValue());
                    speed = minion.getBattleStats().getCurrentSpeed();
                    level = minion.getLevel();
                } else if (minion.getBattleStats().getCurrentSpeed() == speed) {
                    if (minion.getLevel() > level) {
                        // If same speed, but higher level,
                        // Clear, add, and update speed and level
                        fastestMoves.getMoves().clear();
                        fastestMoves.put(playerMoves.getKey(), minionMove.getKey(), minionMove.getValue());
                        speed = minion.getBattleStats().getCurrentSpeed();
                        level = minion.getLevel();
                    } else if (minion.getLevel() == level) {
                        // If same speed and level, add
                        fastestMoves.put(playerMoves.getKey(), minionMove.getKey(), minionMove.getValue());
                    }
                }
            }
        }

        return fastestMoves;
    }



    /**
     * Gets an avatar in this from a key.
     * @param key key to get from
     * @return the found avatar or null if not found
     */
    private BattleAvatar getAvatar(final String key) {
        final BattleAvatar avatar = teamOne.get(key);

        if (avatar != null) {
            return avatar;
        }

        return teamTwo.get(key);
    }

    /**
     * Add a player minion synchronized.
     * @param playerKey key of player controlling the minion
     * @param minionKey key of the minion to add
     * @param minion minion to add
     */
    public synchronized void addSynchronized(final String playerKey, final String minionKey, final PlayerMinion minion) {
        BattleAvatar avatar;

        if ((avatar = teamOne.get(playerKey)) != null) {
            avatar.addMinion(minionKey, minion);
            return;
        } else if((avatar = teamTwo.get(playerKey)) != null) {
            avatar.addMinion(minionKey, minion);
            return;
        }

        throw new RuntimeException();
    }

    /**
     * Takes an attacker and target and performs a move from this based on the attackers calculateMove method
     * @param attacker two keys used to identify a specific minion in a battlestate that represents the minion performing the move
     * @param target two keys used to identify a specific minion in a battlestate that represents the target of the move
     */
    public void performAndReportMove(final BattleMinionIdentifier attacker, final BattleMinionIdentifier target){
        System.out.println("TOB, BattleState, performAndReportMove, " + attacker.getAvatarKey() + ", " + attacker.getMinionKey() + ", " + target.getAvatarKey() + ", " + target.getMinionKey());

        final Minion minionAttacker;
        final BattleMove move;
        BattleAvatar avatar;

        if((avatar = getTeamOne().get(attacker.getAvatarKey())) != null){
            if((minionAttacker = avatar.getBattleMinions().get(attacker.getMinionKey())) != null) {
                move = minionAttacker.getTypeClass().calculateMove(this, attacker, target, true);
                applyMove(move, true);
            }
        } else if ((avatar = getTeamTwo().get(attacker.getAvatarKey())) != null) {
            if((minionAttacker = avatar.getBattleMinions().get(attacker.getMinionKey())) != null) {
                move = minionAttacker.getTypeClass().calculateMove(this, attacker, target, false);
                applyMove(move, false);
            }
        }
    }

    /**
     * Applies moves to the state
     * @param move that needs to be applied
     * @param isTeamOne a value that determines if the move is being performed by a minion on team one
     */
    private void applyMove(final BattleMove move, final boolean isTeamOne){
        if(move == null){
            return;
        }

        final Minion target;
        if(isTeamOne){
            target = teamTwo.get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey());
            target.battleStats.setCurrentHP(target.battleStats.getCurrentHP() - move.getMoveValue());
        } else {
            target = teamOne.get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey());
            target.battleStats.setCurrentHP(target.battleStats.getCurrentHP() - move.getMoveValue());
        }
        moves.add(move);
    }

    /**
     * An iterator that iterators through entries on teamOne and teamTwo
     * @return an iterator of map entries
     */
    public Iterator<Map.Entry<String, BattleAvatar>> EntryIterator() {
        final Set<Map.Entry<String, BattleAvatar>> avatars = new HashSet<>();
        avatars.addAll(teamOne.entrySet());
        avatars.addAll(teamTwo.entrySet());
        return avatars.iterator();
    }
}
