package model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleAvatar {
    List<Minion> battleMinions;
    public String userId;

    public BattleAvatar(List<PlayerMinion> minions, String playerId){
        battleMinions = new ArrayList<>();

        this.userId = playerId;

        for(Minion minion : minions){
            minion.battleStats = new BattleStats(minion);
            battleMinions.add(minion);
        }
    }

    public BattleAvatar(EMinion eMinion){
        battleMinions = new ArrayList<>();

        eMinion.battleStats = new BattleStats(eMinion);
        battleMinions.add(eMinion);
    }

    public BattleAvatar(){
    }

    public List<Minion> getBattleMinions(){ return battleMinions; }

    public String getUserId() { return userId; }

    @Exclude
    public boolean isPlayerControlled() {
        return userId != null;
    }
}
