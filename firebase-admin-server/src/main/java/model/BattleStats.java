package model;

import com.google.firebase.database.Exclude;

/**
 * Contains current stats of a minion doing a battle
 */
public class BattleStats {
    private int currentHP;
    private int currentSpeed;
    private int currentPower;

    /**
     * Constructor for BattleStats
     * @param minion minion the stats are generated from
     */
    public BattleStats(final Minion minion){
        currentHP = minion.health;
        currentSpeed = minion.speed;
        currentPower = minion.power;
    }

    /**
     * Default Constructor
     */
    private BattleStats(){
    }

    /**
     * Getter for currentHP
     * @return currentHP
     */
    public int getCurrentHP(){return currentHP; }

    /**
     * Getter for the currentSpeed
     * @return currentSpeed
     */
    public int getCurrentSpeed(){return currentSpeed; }

    /**
     * Getter for the currentPower
     * @return currentPower
     */
    public int getCurrentPower(){return currentPower; }

    /**
     * Setter for currentHP
     * @param currentHP that the HP is to be set to
     */
    public void setCurrentHP(final int currentHP) { this.currentHP = currentHP; }

    /**
     * Gets if this is alive
     * @return true if, and only if, this is alive
     */
    @Exclude
    public boolean isAlive() {
        return currentHP > 0;
    }
}
