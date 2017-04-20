package model;
import java.util.List;

/**
 * Created by Chres on 18-04-2017.
 */
public abstract class Type {
    protected String id;

    public abstract List<Minion> targets(BattleState battleState);

    public String getId() { return id; }
}
