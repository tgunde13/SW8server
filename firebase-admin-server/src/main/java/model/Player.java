package model;

import java.util.List;
import java.util.Map;

/**
 * Created by Tobias on 31/03/2017.
 */
public class Player {
    public String name;
    public int id;
    public Map<String, PlayerMinion> minions;

    public Player() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public Map<String, PlayerMinion> getMinions() {
        return minions;
    }
}
