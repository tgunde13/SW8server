package model;

import battle.AvatarChoices;
import battle.ChosenMove;

import javax.annotation.Nonnull;
import java.util.*;


public class BattleState {
    private Map<String, BattleAvatar> teamOne;
    private Map<String, BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(final Map<String, BattleAvatar> teamOne, final Map<String, BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
        moves = new ArrayList<BattleMove>();
    }

    private BattleState() {
    }

    public Map<String, BattleAvatar> getTeamOne() { return teamOne; }
    public Map<String, BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }

    /**
     * Gets if the battle is over.
     * @return true if, and only if, the battle is over
     */
    public boolean isOver() {

        Iterator<BattleAvatar> iterator = valueIterator();
        while (iterator.hasNext()){
            BattleAvatar avatar = iterator.next();
            if (avatar.isPlayerControlled() && avatar.hasAliveMinions()) {
                return false;
            }
        }

        return true;
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
    public void performAndReportMove(BattleMinionIdentifier attacker, BattleMinionIdentifier target){
        System.out.println("TOB, BattleState, performAndReportMove, " + attacker.getAvatarKey() + ", " + attacker.getMinionKey());
        Minion minionAttacker;
        BattleMove move;
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

    private void applyMove(BattleMove move, boolean isTeamOne){
        if(move == null){
            return;
        }

        Minion target;
        if(isTeamOne){
            target = teamTwo.get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey());
            target.health -= move.getMoveValue();
        } else {
            target = teamOne.get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey());
            target.health -= 100;
        }
        moves.add(move);
    }

    @Nonnull
    public Iterator<Map.Entry<String, BattleAvatar>> EntryIterator() {
        Set<Map.Entry<String, BattleAvatar>> avatars = new HashSet<>();
        avatars.addAll(teamOne.entrySet());
        avatars.addAll(teamTwo.entrySet());
        return avatars.iterator();
    }

    @Nonnull
    public Iterator<BattleAvatar> valueIterator() {
        List<BattleAvatar> avatars = new ArrayList<>();
        avatars.addAll(teamOne.values());
        avatars.addAll(teamTwo.values());
        return avatars.iterator();
    }
}
