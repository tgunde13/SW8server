package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleMove {
    //First minion in list is the minion doing the move, the rest are targets
    private List<Minion> minions;

    private int abilityValue;

    public BattleMove(List<Minion> minions, int abilityValue){
        this.minions = minions;
        this.abilityValue = abilityValue;
    }

    private BattleMove(){
    }

    public List<Minion> getMinions() { return minions;}

    public int getAbilityValue() {return abilityValue;}
}
