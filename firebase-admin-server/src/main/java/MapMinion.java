/**
 * Created by lapiki on 3/14/17.
 */
abstract class MapMinion {
    String type;
    int maxHealth;
    int currentHealth;
    int level;
    double lon;
    double lat;

    public MapMinion(int level, double lon, double lat){
        this.level = level;
        this.lon = lon;
        this.lat = lat;
    }

    public MapMinion() {}

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