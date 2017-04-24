package model;

/**
 * Created by Tobias on 31/03/2017.
 */
public class PlayerMinion extends Minion {
    private int xp;

    public PlayerMinion(final String name, final int health, final int speed, final int power, final int level, final String type){
        super(name, health, speed, power, level, type);
        xp = 0;
    }

    private PlayerMinion() {}

    public int getXp() {
        return xp;
    }
}
