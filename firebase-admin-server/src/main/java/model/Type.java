package model;
import java.util.List;

/**
 * Represents a specific minion type used to determine specific rules for moves
 */
public abstract class Type {

    /**
     * Abstract method, method is meant to calculate a move
     * @param battleState the state the move is performed in
     * @param target the target of the move
     * @param attacker the minion performing the move
     * @param isTeamOne a value to determine if the attacker is on team one, makes searching easier
     * @return the calculated move, however the move is not applied yet
     */
    public abstract BattleMove calculateMove(BattleState battleState, BattleMinionIdentifier attacker, BattleMinionIdentifier target, boolean isTeamOne);

    /**
     * Abstract method, method is meant to calculate possible targets
     * @param battleState the state the move is performed in
     * @param attackerId the BattleMinionIdentifier of the minion performing the move
     * @return a list of possible targets
     */
    public abstract List<BattleMinionIdentifier> getAvailableMoves(BattleState battleState, BattleMinionIdentifier attackerId);
}
