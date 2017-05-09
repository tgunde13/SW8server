package model;
import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */
public abstract class Type {
    protected String id;

    public abstract BattleMove calculateMove(BattleState battleState, BattleMinionIdentifier attacker, BattleMinionIdentifier target, boolean isTeamOne);

    public abstract List<BattleMinionIdentifier> getAvailableMoves(BattleState battleState, BattleMinionIdentifier attackerId);

    public String getId() { return id; }
}
