package model;

import java.util.HashMap;
import java.util.Map;

/**
 * Melee type for minions
 */
public class MeleeType extends Type {

    public MeleeType(){ id = "Melee"; }



    public BattleMove calculateMove(BattleState battleState, BattleMinionIdentifier target, BattleMinionIdentifier attacker, boolean isTeamOne){
       if(isLegal(battleState, target, isTeamOne)){

           BattleMove move = new BattleMove(target, attacker, 0);
       }

       return null;
    }

    /**
     * Takes a move and a battleState and checks whether the move is legal in the given battleState
     * @param battleState the current battleState we want to check in
     * @param move battleMove that represents the move we want to check for
     * @return true if the move is legal or false if the move is illegal
     */
    private boolean isLegal(BattleState battleState, BattleMinionIdentifier target, boolean isTeamOne){
        Minion attacker;

        if(isTeamOne){
            if((battleState.getTeamTwo().get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey())) != null){
                return true;
            } else {
                return false;
            }
        } else if((attacker = battleState.getTeamTwo().get(move.getAttacker().getAvatarKey()).getBattleMinions().get(move.getAttacker().getMinionKey())) != null){
            if((battleState.getTeamOne().get(move.getTarget().getAvatarKey()).getBattleMinions().get(move.getTarget().getMinionKey())) != null){
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }


    /**
     * Takes a minion key and a battleState and checks which minions are valid targets for the given minion in the battleState
     * @param battleState current battleState
     * @param identifier the minions key in its corresponding hashmap
     * @return a list of possible targets for the minion with the given key in the given battleState
     */
    private Map<String, Minion> isTarget(final BattleState battleState, BattleMinionIdentifier identifier){
        HashMap<String, Minion> targets = new HashMap<>();

        for (Map.Entry<String, BattleAvatar> battleAvatarEntry: battleState.getTeamOne().entrySet()) {
            if (null != battleAvatarEntry.getValue().getBattleMinions().get(identifier.getMinionKey())){
                for (Map.Entry<String, BattleAvatar> battleAvatarEntryTwo: battleState.getTeamOne().entrySet()) {
                    targets.putAll(battleAvatarEntryTwo.getValue().getBattleMinions());
                }
                return targets;
            }
        }
        for (Map.Entry<String, BattleAvatar> battleAvatarEntry: battleState.getTeamOne().entrySet()) {
            targets.putAll(battleAvatarEntry.getValue().getBattleMinions());
        }
        return targets;
    }
}
