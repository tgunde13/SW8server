import model.MapMinion;

/**
 * Created by lapiki on 3/14/17.
 */
public class Footman extends MapMinion {


    public Footman(int level, double lon, double lat) {
        super(level, lon, lat);
        type = "Footman";
        maxHealth = 100*level;
        currentHealth = maxHealth;
    }

    public Footman() {}
}
