package model;

import com.google.firebase.database.Exclude;

/**
 * Represents a minion in our system
 */
public class Minion {
    static final int MAX_LEVEL = 50;
    String name;
    int health;
    int power;
    int speed;
    int level;
    String type;
    protected BattleStats battleStats;
    private Type typeClass;

    /**
     * Constructor for a minion
     * @param name of the minion
     * @param health of the minion
     * @param speed of the minion
     * @param power of the miniom
     * @param level of the minion
     * @param type of minion
     */

    Minion(final String name, final int health, final int speed, final int power, final int level, final String type){
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.power = power;
        this.level = level;
        this.type = type;
    }

    /**
     * Assign the type class to the minion, determined from the type string
     */
    public void assignTypeClass(){
        switch (type){
            case "Melee":
                typeClass = new MeleeType();
        }
    }

    /**
     * Default constructor, used for firebase
     */
    protected Minion() {}

    /**
     * Getter for name
     * @return name of the minion
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for health
     * @return health of the minion
     */
    public int getHealth() { return health; }

    /**
     * Getter for speed
     * @return speed of the minion
     */
    public int getSpeed() { return speed; }

    /**
     * Getter for power
     * @return power of the minion
     */
    public int getPower() { return power; }

    /**
     * Getter for level
     * @return level of the minion
     */
    public int getLevel() {
        return level;
    }

    /**
     * Getter for the type
     * @return type of minion
     */
    public String getType() {
        return type;
    }

    /**
     * Getter for the BattleStats of the minion
     * @return battlestats of the minion
     */
    public BattleStats getBattleStats() { return battleStats; }

    /**
     * Method for creating BattleStats for a minion, used when a minion enters a battle
     */
    public void createBattleStats() {
        battleStats = new BattleStats(this);
    }

    /**
     * Getter for the type class associated with this minion
     * @return the type class associated with this minion
     */
    @Exclude
    public Type getTypeClass() {
        return typeClass;
    }

    /**
     * Gets if this is alive.
     * This must have a battle stats before calling this.
     * @return true if, and only if, this is alive
     */
    @Exclude
    boolean isAlive() {
        return battleStats.isAlive();
    }
}
