package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public abstract class BattleAvatar {
    List<BattleMinion> battleMinions;

    BattleAvatar(List<Minion> minions){
        for(Minion minion : minions){
            battleMinions.add(new BattleMinion(minion));
        }
    }

    BattleAvatar(){
    }

    List<BattleMinion> getBattleMinions(){ return battleMinions; }
}
