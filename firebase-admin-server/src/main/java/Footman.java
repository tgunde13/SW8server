/**
 * Created by lapiki on 3/14/17.
 */
public class Footman extends Minion {


    public Footman(int level, double lon, double lat) {
        super(level, lon, lat);
        maxHealth = 100*level;
        currentHealth = maxHealth;
    }

    public Footman() {}
}
