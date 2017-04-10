package model;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Minion {
    public String type;
    public int maxHealth;
    public int currentHealth;
    public int level;

    public Minion() {}

    public String getType() {
        return type;
    }

    public int getMaxHealth() {
        return maxHealth;
    }

    public int getCurrentHealth() {
        return currentHealth;
    }

    public int getLevel() {
        return level;
    }
}
