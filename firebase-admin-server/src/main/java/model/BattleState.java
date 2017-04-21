package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    private List<BattleAvatar> teamOne;
    private List<BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(List<BattleAvatar> teamOne, List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    private BattleState(){
    }

    public List<BattleAvatar> getTeamOne() { return teamOne; }
    public List<BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }

    public void advance(final Map<String, String> chosenMoves) {
        // TODO
    }
}
