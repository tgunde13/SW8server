package model;

import com.google.firebase.database.Exclude;

/**
 * Created by Chres on 18-04-2017.
 */
public class BattleStats {
    private int currentHP;
    private int currentSpeed;
    private int currentPower;

    public BattleStats(final Minion minion){
        currentHP = minion.health;
        currentSpeed = minion.speed;
        currentPower = minion.power;
    }

    private BattleStats(){
    }

    public int getCurrentHP(){return currentHP; }
    public int getCurrentSpeed(){return currentSpeed; }
    public int getCurrentPower(){return currentPower; }

    public void setCurrentHP(final int currentHP) { this.currentHP = currentHP; }
    public void setCurrentPower(final int currentPower) { this.currentPower = currentPower; }
    public void setCurrentSpeed(final int currentSpeed) { this.currentSpeed = currentSpeed; }

    /**
     * Gets if this is alive
     * @return true if, and only if, this is alive
     */
    @Exclude
    public boolean isAlive() {
        return currentHP > 0;
    }
}
