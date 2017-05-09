package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Player {
    private String name;
    private int id;
    private int gold = 0;
    private Map<String, PlayerMinion> minions;

    public Player(final String name, final int id) {
        this.name = name;
        this.id = id;
    }

    private Player() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getGold() {return gold;}

    public Map<String, PlayerMinion> getMinions() { return minions; }
}
