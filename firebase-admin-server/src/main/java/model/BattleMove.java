package model;

/**
 * Class that represents a moveValue made by a minion
 */
public class BattleMove {
    private BattleMinionIdentifier attackingMinionKey;
    private BattleMinionIdentifier targetMinionKey;
    private int moveValue;

    /**
     * Constructor for BattleMove
     * @param attackingMinionKey key of the minion that is making the moveValue
     * @param targetMinionKeys key of the minion that is the target of the moveValue
     * @param moveValue the value of the moveValue
     */
    public BattleMove(final BattleMinionIdentifier attackingMinionKey, final BattleMinionIdentifier targetMinionKeys, final int moveValue){
        this.attackingMinionKey = attackingMinionKey;
        this.targetMinionKey = targetMinionKeys;
        this.moveValue = moveValue;

    }

    /**
     * Default constructor, used by firebase
     */
    private BattleMove(){
    }

    /**
     * Getter for attackingMinionKey
     * @return the key of the attacking minion
     */
    public BattleMinionIdentifier getAttacker(){ return  attackingMinionKey;}

    /**
     * Getter for targetMinionKey
     * @return the key of the target minion
     */
    public BattleMinionIdentifier getTarget() { return targetMinionKey;}

    /**
     * Getter for ability value
     * @return the value associated with this moveValue
     */
    public int getMoveValue() {
        return moveValue;
    }
}
