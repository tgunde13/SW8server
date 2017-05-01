package model;

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

    private BattleState(){

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
     * Advances to the next turn by performance moves.
     * Minions with higher speed performs moves first.
     * Minions with same speed, but higher level, performs moves first.
     * Minions with same speed and level performs moves in a random order.
     * @param chosenMoves the moves to perform
     */
    public void advance(final Map<String, Map<String, ChosenMove>> chosenMoves) {
        /*// TODO

        int speed = 0, level = 0;
        Map<String, Map<String, ChosenMove>> fastestMoves = new HashMap<>();

        for (Map.Entry<String, Map<String, ChosenMove>> playerMoves : chosenMoves.entrySet()) {
            for (Map.Entry<String, ChosenMove> minionMove : playerMoves.getValue().entrySet()) {
                // If should act before previous
                if (teamOne.)
            }
        }*/
    }

    /**
     * Add a player minion synchronized.
     * @param playerKey key of player controlling the minion
     * @param minionKey key of the minion to add
     * @param minion minion to add
     */
    public synchronized void addSynchronized(final String playerKey, final String minionKey, final PlayerMinion minion) {
        Iterator<BattleAvatar> iterator = valueIterator();
        while (iterator.hasNext()){
            BattleAvatar avatar = iterator.next();
            if (avatar.isPlayerControlled()) {
                avatar.getBattleMinions().put(minionKey, minion);
                return;
            }
        }

        throw new RuntimeException();
    }

    /**
     * Takes an attacker and target and performs a move from this based on the attackers calculateMove method
     * @param attacker two keys used to identify a specific minion in a battlestate that represents the minion performing the move
     * @param target two keys used to identify a specific minion in a battlestate that represents the target of the move
     */
    public void doMove(BattleMinionIdentifier attacker, BattleMinionIdentifier target){
        Minion minionAttacker;
        BattleMove move;

        if(getTeamOne().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey()) != null){
            minionAttacker = getTeamOne().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey());
            move = minionAttacker.getTypeClass().calculateMove(this, attacker, target, true);
            applyMove(move, true);
        } else if (getTeamTwo().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey()) != null) {
            minionAttacker = getTeamTwo().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey());
            move = minionAttacker.getTypeClass().calculateMove(this, attacker, target, false);
            applyMove(move, false);
        }
    }

    private void applyMove(BattleMove move, boolean isTeamOne){
        Minion target;
        if(isTeamOne){
            target = teamTwo.get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey());
            target.health -= move.getMoveValue();
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
