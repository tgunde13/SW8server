import java.util.Random;

/**
 * Created by lapiki on 3/14/17.
 */
public class Generator {
    Random rand = new Random();


    public Minion generateMinion(){
        int level = rand.nextInt(50) + 1;
        double lon = (rand.nextDouble() * 2) + 56;
        double lat = (rand.nextDouble() * 3) + 8;

        return generateMinionOfTypeType(level, lon, lat);
    }

    public Minion generateMinionOfTypeType(int level, double lon, double lat) {

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
