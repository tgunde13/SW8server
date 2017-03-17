/**
 * Created by lapiki on 3/14/17.
 */
public class Spearman extends Minion {

    public Spearman(int level, double lon, double lat) {
        super(level, lon, lat);
        maxHealth = 200*level;
        currentHealth = maxHealth;
    }

    public Spearman() {}
}
