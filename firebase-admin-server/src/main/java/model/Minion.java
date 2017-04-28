package model;

import com.google.firebase.database.Exclude;

import java.util.Random;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Minion {
    private static final int MAX_LEVEL = 50;
    protected String name;
    protected int health;
    protected int power;
    protected int speed;
    protected int level;
    protected String type;
    protected BattleStats battleStats;
    protected Type typeClass;

    /*public Minion(String fileName){
        //Insert system to read files
    }*/

    public Minion(final String name, final int health, final int speed, final int power, final int level, final String type){
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.power = power;
        this.level = level;
        this.type = type;
    }

    public void assignTypeClass(){
        switch (type){
            case "melee":
                typeClass = new MeleeType();
        }
    }

    public Minion() {}

    public String getName() {
        return name;
    }

    public int getHealth() { return health; }

    public int getSpeed() { return speed; }

    public int getPower() { return power; }

    public int getLevel() {
        return level;
    }

    public String getType() {
        return type;
    }

    public BattleStats getBattleStats() { return battleStats; }

    public void createBattleStats() {
        battleStats = new BattleStats(this);
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

    //public void setBattleStats(BattleStats battleStats) {this.battleStats = battleStats;}
}
