package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    private List<BattleAvatar> teamOne;
    private List<BattleAvatar> teamTwo;
    private List<BattleMove> moves;

    public BattleState(final List<BattleAvatar> teamOne, final List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;

        int turn = 0;
    }

    private BattleState(){
    }

    public List<BattleAvatar> getTeamOne() { return teamOne; }
    public List<BattleAvatar> getTeamTwo() { return teamTwo; }
    public List<BattleMove> getMoves() { return moves; }
}
