import java.util.Random;

/**
 * A class that can generate a Minion object.
 */
public class Generator {
    Random rand = new Random();


    /**
     * Generates a Minion object.
     * @return The generated Minion object.
     */
    public Minion generateMinion(){
        int level = rand.nextInt(50) + 1;

        //Coordinates that are restricted to north jutland.
        double lon = (rand.nextDouble()) + 55.5;
        double lat = (rand.nextDouble()) + 9.5;

        return generateMinionOfType(level, lon, lat);
    }

    /**
     * Generates a Minion object of a rondom type.
     * @param level The level og the Minion.
     * @param lon Longitude of the Minion.
     * @param lat Latitude of the Minion.
     * @return Returns the Minion Object.
     */
    public Minion generateMinionOfType(int level, double lon, double lat) {

        int num = rand.nextInt(2);
        Minion m = null;
        switch (num) {
            case 0:  m = new Footman(level, lon, lat);
                break;
            case 1:  m = new Spearman(level, lon, lat);
                break;
        }
        return m;
    }
}
