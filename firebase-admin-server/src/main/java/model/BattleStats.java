package model;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleStats {
    public int currentHP;
    public int currentSpeed;
    public int currentPower;

    public BattleStats(Minion minion){
        currentHP = minion.health;
        currentSpeed = minion.speed;
        currentPower = minion.power;
    }

    private BattleStats(){
    }

    public int getCurrentHP(){return currentHP; }
    public int getCurrentSpeced(){return currentSpeed; }
    public int getCurrentPower(){return currentPower; }
}
