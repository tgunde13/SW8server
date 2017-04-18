package model;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleStats {
    int currentHP;
    int currentSpeed;
    int currentPower;

    BattleStats(Minion minion){
        currentHP = minion.health;
        currentSpeed = minion.health;
        currentPower = minion.health;
    }

    private BattleStats(){
    }

    public int getCurrentHP(){return currentHP; }
    public int getCurrentSpeced(){return currentSpeed; }
    public int getCurrentPower(){return currentPower; }
}
