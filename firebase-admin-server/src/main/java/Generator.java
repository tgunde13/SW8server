import java.util.Random;

/**
 * A class that can generate a MapMinion object.
 */
public class Generator {
    Random rand = new Random();


    /**
     * Generates a MapMinion object.
     * @return The generated MapMinion object.
     */
    public MapMinion generateMinion(){
        int level = rand.nextInt(50) + 1;

        //Coordinates that are restricted to north jutland.
        double lon = (rand.nextDouble()) + 55.5;
        double lat = (rand.nextDouble()) + 9.5;

        return generateMinionOfType(level, lon, lat);
    }

    /**
     * Generates a MapMinion object of a rondom type.
     * @param level The level og the MapMinion.
     * @param lon Longitude of the MapMinion.
     * @param lat Latitude of the MapMinion.
     * @return Returns the MapMinion Object.
     */
    public MapMinion generateMinionOfType(int level, double lon, double lat) {

        int num = rand.nextInt(2);
        MapMinion m = null;
        switch (num) {
            case 0:  m = new Footman(level, lon, lat);
                break;
            case 1:  m = new Spearman(level, lon, lat);
                break;
        }
        return m;
    }
}
