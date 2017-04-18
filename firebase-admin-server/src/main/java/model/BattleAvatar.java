package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleAvatar {
    List<Minion> battleMinions;
    public String userId;

    public BattleAvatar(List<PlayerMinion> minions, String playerId){
        this.userId = playerId;

        for(Minion minion : minions){
            minion.battleStats = new BattleStats(minion);
            battleMinions.add(minion);
        }
    }

    public BattleAvatar(EMinion eMinion){
        eMinion.battleStats = new BattleStats(eMinion);
        battleMinions.add(eMinion);
    }

    public BattleAvatar(){
    }

    List<Minion> getBattleMinions(){ return battleMinions; }

    public String getId() { return userId; }
}
