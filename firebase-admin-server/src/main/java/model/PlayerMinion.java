package model;

/**
 * Created by Tobias on 31/03/2017.
 */
public class PlayerMinion extends Minion {
    public int xp;

    public PlayerMinion(String name, int health, int speed, int power, int level, String type){
        super(name, health, speed, power, level, type);
        xp = 0;
    }

    private PlayerMinion() {}

    public int getXp() {
        return xp;
    }
}
