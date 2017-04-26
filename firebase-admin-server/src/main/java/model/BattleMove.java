package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleMove {
    //First minion in list is the minion doing the move, the rest are targets
    private List<String> minionKeys;

    private int abilityValue;

    public BattleMove(final List<String> minionKeys, final int abilityValue){
        this.minionKeys = minionKeys;
        this.abilityValue = abilityValue;
    }

    private BattleMove(){
    }

    public List<String> getMinionKeys() { return minionKeys;}

    public int getAbilityValue() {return abilityValue;}
}
