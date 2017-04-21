package model;

import java.util.Random;

/**
 * A minion of the map.
 */
public class EMinion extends Minion {
    private static final int MAX_LEVEL = 50;
    private double lon;
    private double lat;

    /**
     * Constructs a map minion with a given coordinate.
     * The minion has a random level and type.
     * @param lat the latitude of the minion
     * @param lon the longitude of the minion
     */
    public EMinion(final double lat, final double lon) {
        this.lat = lat;
        this.lon = lon;

        generateEMinionData();
    }

    public void generateEMinionData() {

        level = new Random().nextInt(MAX_LEVEL) + 1;

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
        }
    }

    private EMinion() {}

    public double getLon(){
        return lon;
    }

    public double getLat(){
        return lat;
    }

    public void setLat(final double lat) { this.lat = lat; }

    public void setLon(final double lon) {this.lon = lon;}
}