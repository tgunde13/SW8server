package model;

import java.util.ArrayList;

/**
 * Created by Chres on 18-04-2017.
 */



public class MeleeType extends Type {

    MeleeType(){ id = "Melee"; }

    public ArrayList<Minion> targets(BattleState battleState){
        return null;
    }
}
