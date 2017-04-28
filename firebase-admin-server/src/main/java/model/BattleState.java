package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    private Map<String, BattleAvatar> teamOne;
    private Map<String, BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(final Map<String, BattleAvatar> teamOne, final Map<String, BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;

        int turn = 0;
    }

    private BattleState(){
    }

    public Map<String, BattleAvatar> getTeamOne() { return teamOne; }
    public Map<String, BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }
}
