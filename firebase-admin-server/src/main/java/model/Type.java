package model;

import java.util.ArrayList;

/**
 * Created by Chres on 18-04-2017.
 */
public abstract class Type {
    String id;

    public abstract ArrayList<Minion> targets(BattleState battleState);
}
