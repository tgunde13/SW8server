package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleMove {
    //First minion is list the minion doing the move, the rest are targets
    public List<Minion> minions;

    public int abilityValue;

    public BattleMove(List<Minion> minions, int abilityValue){
        this.minions = minions;
        this.abilityValue = abilityValue;
    }

    public BattleMove(){
    }

    public List<Minion> getMinions() { return minions;}

    public int getAbilityValue() {return abilityValue;}
}
