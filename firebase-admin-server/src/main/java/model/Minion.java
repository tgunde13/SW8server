package model;

import java.util.Random;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Minion {
    private static final int MAX_LEVEL = 50;
    public String name;
    public int health;
    public int power;
    public int speed;
    public int level;
    public Role role;
    public Type type;

    public void generateEMinionData() {
        role = new ERole();

        level = new Random().nextInt(MAX_LEVEL) + 1;

        switch (new Random().nextInt(2)) {
            case 0:
                name = "Swordman";
                type = new MeleeType();
                health = 100*level;
                speed = 20;
                power = 40*level;
                break;
            case 1:
                name = "Spearman";
                type = new MeleeType();
                health = 200*level;
                speed = 10;
                power = 20*level;

                break;
        }
    }

    public Minion() {}

    public String getName() {
        return name;
    }

    public int getHealth() {
        return health;
    }

    public int getLevel() {
        return level;
    }

    public Role getRole() {
        return role;
    }

    public Type getType() {
        return type;
    }
}
