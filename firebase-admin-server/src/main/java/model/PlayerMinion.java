package model;

/**
 * Created by Tobias on 31/03/2017.
 */
public class PlayerMinion extends Minion {
    private final int xp = 0;

    public PlayerMinion(final String name, final int health, final int speed, final int power, final int level, final String type){
        super(name, health, speed, power, level, type);
    }

    private PlayerMinion() {}

    public int getXp() {
        return xp;
    }
}
