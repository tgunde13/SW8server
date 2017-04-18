package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleState {
    List<BattleAvatar> teamOne;
    List<BattleAvatar> teamTwo;

    List<BattleMove> moves;

    BattleState(List<BattleAvatar> teamOne, List<BattleAvatar> teamTwo){
        this.teamOne = teamOne;
        this.teamTwo = teamTwo;
    }

    BattleState(){
    }

    List<BattleAvatar> getTeamOne() { return teamOne; }
    List<BattleAvatar> getTeamTwo() { return teamTwo; }
    List<BattleMove> getMoves() { return moves; }
}
