package model;
import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */
public abstract class Type {
    protected String id;

    public abstract boolean isLegal(BattleState battleState, BattleMove move);

    public String getId() { return id; }
}
