package model;

import java.util.Random;

/**
 * A minion of the map.
 */
public class MapMinion {
    private static final int MAX_LEVEL = 50;

    protected String type;
    protected int maxHealth;
    protected int currentHealth;
    int level;
    double lon;
    double lat;

    public MapMinion(int level, double lon, double lat){
        this.level = level;
        this.lon = lon;
        this.lat = lat;
    }

    /**
     * Constructs a map minion with a given coordinate.
     * The minion has a random level and type.
     * @param lat the latitude of the minion
     * @param lon the longitude of the minion
     */
    MapMinion(double lat, double lon) {
        level = new Random().nextInt(MAX_LEVEL) + 1;

        this.lat = lat;
        this.lon = lon;

        switch (new Random().nextInt(2)) {
            case 0:
                type = "Footman";
                maxHealth = 100*level;
                break;
            case 1:
                type = "Spearman";
                maxHealth = 200*level;
                break;
        }
    }

    public MapMinion() {}

    public int getMaxHealth(){
        return maxHealth;
    }

    public int getCurrentHealth(){
        return currentHealth;
    }

    public int getLevel(){
        return level;
    }

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }
}