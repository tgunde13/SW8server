package model;

import java.util.Random;

/**
 * A minion of the map.
 */
public class EMinionTemplate extends Minion {
    private static final int MAX_LEVEL = 50;
    private final double lon;
    private final double lat;
    private final int size;

    /**
     * Constructs a map minion with a given coordinate.
     * The minion has a random level and type.
     * @param lat the latitude of the minion
     * @param lon the longitude of the minion
     */
    public EMinionTemplate(final double lat, final double lon, int size, String name, int health, int speed, int power, int level, String type) {
        super(name, health, speed, power, level, type);
        this.lat = lat;
        this.lon = lon;
        this.size = size;
    }

    public static EMinionTemplate generateEMinionData(double lat, double lon) {

        int level;
        int size;
        String name;
        String type;
        int health;
        int speed;
        int power;


        level = new Random().nextInt(MAX_LEVEL) + 1;
        if(level < 10){
            size = 1;
        } else if(level < 20){
            size = 2;
        } else {
            size = 3;
        }
        switch (new Random().nextInt(2)) {
            case 0:
                name = "Swordman";
                type = "Melee";
                health = 100*level;
                speed = 20;
                power = 40*level;
                break;
            case 1:
                name = "Spearman";
                type = "Melee";
                health = 200*level;
                speed = 10;
                power = 20*level;
                break;
            default:
                name = "Unknown";
                type = "Melee";
                health = 1*level;
                speed = 1;
                power = 1*level;
                break;
        }
        return new EMinionTemplate(lat, lon, size, name, health, speed, power, level, type);
    }

    public Minion createMinion(){
        return new Minion(name, health, speed, power, level, type);
    }

    private EMinionTemplate() {
        lon = 0;
        lat = 0;
        size = 0;
    }

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }
    public int getSize(){
        return size;
    }
}