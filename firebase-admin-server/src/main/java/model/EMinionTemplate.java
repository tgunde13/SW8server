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
     * @param size of the templates, determine how many minions are to be created when this template is engaged in a
     * battle
     * @param name of the minion
     * @param health of the minion
     * @param speed of the minion
     * @param power of the minion
     * @param level of the minion
     * @param type of minion
     */
    public EMinionTemplate(final double lat, final double lon, final int size, final String name, final int health, final int speed, final int power, final int level, final String type) {
        super(name, health, speed, power, level, type);
        this.lat = lat;
        this.lon = lon;
        this.size = size;
    }

    /**
     * Method to generate a random minion on a position
     * @param lat of the position
     * @param lon of the position
     * @return a EMinionTemplate
     */
    public static EMinionTemplate generateEMinionData(final double lat, final double lon) {

        final int level;
        final int size;
        final String name;
        final String type;
        final int health;
        final int speed;
        final int power;


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
                power = 35*level;
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
                health = level;
                speed = 1;
                power = level;
                break;
        }
        return new EMinionTemplate(lat, lon, size, name, health, speed, power, level, type);
    }

    /**
     * Creates a minion from this template
     * @return a minion
     */
    public Minion createMinion(){
        return new Minion(name, health, speed, power, level, type);
    }

    /**
     * Dafault constructor
     */
    private EMinionTemplate() {
        lon = 0;
        lat = 0;
        size = 0;
    }

    /**
     * Getter for longtitude
     * @return the longtitude
     */
    public double getLon(){
        return lon;
    }

    /**
     * Getter for latitude
     * @return the latitude
     */
    public double getLat(){
        return lat;
    }

    /**
     * Getter for the size
     * @return the size of the template
     */
    public int getSize(){
        return size;
    }
}