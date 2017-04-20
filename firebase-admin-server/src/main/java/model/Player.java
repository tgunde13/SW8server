package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Player {
    private String name;
    private int id;
    private Map<String, PlayerMinion> minions;

    public Player(String name, int id) {}

    private Player() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Map<String, PlayerMinion> getMinions() { return minions; }

    public void setId(int id) { this.id = id; }

    public void setName(String name) {this.name = name;}

    public void setMinions(Map<String, PlayerMinion> minions) { this.minions = minions; }
}
