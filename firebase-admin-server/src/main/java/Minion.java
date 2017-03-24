/**
 * Created by lapiki on 3/14/17.
 */
abstract class Minion {
    String type;
    int maxHealth;
    int currentHealth;
    int level;
    double lon;
    double lat;

    public Minion(int level, double lon, double lat){
        this.level = level;
        this.lon = lon;
        this.lat = lat;
    }

    public Minion() {}

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