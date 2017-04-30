package model;

import battle.ChosenMove;

import javax.annotation.Nonnull;
import java.util.*;


public class BattleState implements Iterable<BattleAvatar> {
    private Map<String, BattleAvatar> teamOne;
    private Map<String, BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(final Map<String, BattleAvatar> teamOne, final Map<String, BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
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
        for (final BattleAvatar avatar : this) {
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
        // TODO

        int speed = 0, level = 0;
        Map<String, Map<String, ChosenMove>> fastestMoves = new HashMap<>();

        for (Map.Entry<String, Map<String, ChosenMove>> playerMoves : chosenMoves.entrySet()) {
            for (Map.Entry<String, ChosenMove> minionMove : playerMoves.getValue().entrySet()) {
                // If should act before previous
                if (teamOne.)
            }
        }
    }

    /**
     * Add a player minion synchronized.
     * @param playerKey key of player controlling the minion
     * @param minionKey key of the minion to add
     * @param minion minion to add
     */
    public synchronized void addSynchronized(final String playerKey, final String minionKey, final PlayerMinion minion) {
        for (final BattleAvatar avatar : this) {
            if (avatar.isPlayerControlled() && avatar.getUserId().equals(playerKey)) {
                avatar.getBattleMinions().put(minionKey, minion);
                return;
            }
        }

        throw new RuntimeException();
    }

    public void doMove(BattleMinionIdentifier attacker, BattleMinionIdentifier target){

    }

    @Override
    @Nonnull
    public Iterator<BattleAvatar> iterator() {
        final List<BattleAvatar> avatars = new ArrayList<>();
        avatars.addAll(teamOne.values());
        avatars.addAll(teamTwo.values());
        return avatars.iterator();
    }
}
