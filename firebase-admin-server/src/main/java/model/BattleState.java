package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    public List<BattleAvatar> teamOne;
    public List<BattleAvatar> teamTwo;
    public List<BattleMove> moves;

    public BattleState(List<BattleAvatar> teamOne, List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    private BattleState(){
    }

    public List<BattleAvatar> getTeamOne() { return teamOne; }
    public List<BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }
}
