package model;

public class BattleMove {
    private BattleMinionIdentifier attackingMinionKey;
    private BattleMinionIdentifier targetMinionKey;
    private String abilityValue;

    public BattleMove(final BattleMinionIdentifier attackingMinionKey, final BattleMinionIdentifier targetMinionKeys, String abilityValue){
        this.attackingMinionKey = attackingMinionKey;
        this.targetMinionKey = targetMinionKeys;
        this.abilityValue = abilityValue;

    }

    private BattleMove(){
    }



    public BattleMinionIdentifier getAttacker(){ return  attackingMinionKey;}

    public BattleMinionIdentifier getTarget() { return targetMinionKey;}
}
