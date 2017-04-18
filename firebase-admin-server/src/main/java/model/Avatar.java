package model;

import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public class Avatar {
    String name;
    int id;
    List<Minion> minions;

    public Avatar() {}

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public List<Minion> getMinions() {
        return minions;
    }
}
