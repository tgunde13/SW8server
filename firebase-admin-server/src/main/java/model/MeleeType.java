package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Melee type for minions
 */
public class MeleeType extends Type {

    public MeleeType(){ id = "Melee"; }


    /**
     *
     * @param battleState
     * @param target
     * @param attacker
     * @param isTeamOne
     * @return
     */
    public BattleMove calculateMove(BattleState battleState, BattleMinionIdentifier attacker, BattleMinionIdentifier target, boolean isTeamOne){
       if(isLegal(battleState, target, isTeamOne)){
           if(isTeamOne){
               return new BattleMove(attacker, target, battleState.getTeamOne().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey()).power);
           } else {
               return new BattleMove(attacker, target, battleState.getTeamTwo().get(attacker.getAvatarKey()).getBattleMinions().get(attacker.getMinionKey()).power);
           }
       }
       return null;
    }

    /**
     *
     * @param battleState
     * @param target
     * @param isTeamOne
     * @return
     */
    private boolean isLegal(BattleState battleState, BattleMinionIdentifier target, boolean isTeamOne){
        Minion attacker;

        if(isTeamOne){
            if(battleState.getTeamTwo().get(target.getAvatarKey()).getBattleMinions().get(target.getMinionKey()) != null){
                return true;
            } else {
                return false;
            }
        } else {
            if (battleState.getTeamOne().get(target.getAvatarKey()).getBattleMinions().get(target.getMinionKey()) != null) {
                return true;
            } else {
                return false;
            }
        }
    }
}
