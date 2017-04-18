package model;

import java.util.Random;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Minion {
    private static final int MAX_LEVEL = 50;
    public String name;
    public int health;
    public int power;
    public int speed;
    public int level;
    public String type;
    public BattleStats battleStats;

    /*public Minion(String fileName){
        //Insert system to read files
    }*/

    public Minion(String name, int health, int speed, int power, int level, String type){
        this.name = name;
        this.health = health;
        this.speed = speed;
        this.power = power;
        this.level = level;
        this.type = type;
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
}
