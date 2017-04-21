package model;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleStats {
    private int currentHP;
    private int currentSpeed;
    private int currentPower;

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

    public void setCurrentHP(int currentHP) { this.currentHP = currentHP; }
    public void setCurrentPower(int currentPower) { this.currentPower = currentPower; }
    public void setCurrentSpeed(int currentSpeed) { this.currentSpeed = currentSpeed; }

    /**
     * Gets if this is alive
     * @return true if, and only if, this is alive
     */
    public boolean isAlive() {
        return currentHP > 0;
    }
}
