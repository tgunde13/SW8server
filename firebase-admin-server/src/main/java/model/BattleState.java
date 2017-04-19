package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    List<BattleAvatar> teamOne;
    List<BattleAvatar> teamTwo;

    List<BattleMove> moves;

    private int turn;

    public BattleState(List<BattleAvatar> teamOne, List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;

        turn = 0;
    }

    private BattleState(){
    }

    public List<BattleAvatar> getTeamOne() { return teamOne; }
    public List<BattleAvatar> getTeamTwo() { return teamTwo; }
    List<BattleMove> getMoves() { return moves; }

    /**
     * Get turn number.
     * @return turn number
     */
    public int getTurn() {
        return turn;
    }
}
