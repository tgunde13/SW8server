package model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState implements Iterable<BattleAvatar> {
    private List<BattleAvatar> teamOne;
    private List<BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(final List<BattleAvatar> teamOne, final List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    private BattleState(){

    }

    public List<BattleAvatar> getTeamOne() { return teamOne; }
    public List<BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }

    /**
     * Gets if the battle is over.
     * @return true if, and only if, the battle is over
     */
    public boolean isOver() {
        for (final BattleAvatar avatar : teamOne) {
            if (avatar.isPlayerControlled() && avatar.hasAliveMinions()) {
                return false;
            }
        }

        for (final BattleAvatar avatar : teamTwo) {
            if (avatar.isPlayerControlled() && avatar.hasAliveMinions()) {
                return false;
            }
        }

        return true;
    }

    public void advance(final Map<String, Map<String, String>> chosenMoves) {
        // TODO
    }

    public synchronized void addSynchronized(final String playerKey, final String minionKey, final PlayerMinion minion) {
        for (final BattleAvatar avatar : this) {
            if (avatar.isPlayerControlled() && avatar.getUserId().equals(playerKey)) {
                avatar.getBattleMinions().put(minionKey, minion);
                return;
            }
        }

        throw new RuntimeException();
    }

    @Override
    public Iterator<BattleAvatar> iterator() {
        final List<BattleAvatar> avatars = new ArrayList<>();
        avatars.addAll(teamOne);
        avatars.addAll(teamTwo);
        return avatars.iterator();
    }
}
