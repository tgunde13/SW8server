package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
     * Gets a list of BattleMinionIdentifiers that represents available targets
     * @param state is the state that we want to perform a move in
     * @param attacker is the attacker who wants to perform the moves
     * @return a list of BattleMinionIdentifiers, is empty if no moves are available
     */
    public List<BattleMinionIdentifier> getAvailableMoves(BattleState state, BattleMinionIdentifier attacker){
        List<BattleMinionIdentifier> availableMoves = new ArrayList<>();
        if(!(state.getTeamOne().get(attacker.getAvatarKey()) == null)){
            for(Map.Entry<String, BattleAvatar> avatarEntry: state.getTeamTwo().entrySet()){
                for(Map.Entry<String, Minion> minionEntry : avatarEntry.getValue().getBattleMinions().entrySet()){
                    if(minionEntry.getValue().isAlive()){
                        availableMoves.add(new BattleMinionIdentifier(avatarEntry.getKey(), minionEntry.getKey()));
                    }
                }
            }
        } else {
            for(Map.Entry<String, BattleAvatar> avatarEntry: state.getTeamOne().entrySet()){
                for(Map.Entry<String, Minion> minionEntry : avatarEntry.getValue().getBattleMinions().entrySet()){
                    if(minionEntry.getValue().isAlive()){
                        availableMoves.add(new BattleMinionIdentifier(avatarEntry.getKey(), minionEntry.getKey()));
                    }
                }
            }
        }
        return availableMoves;
    }

    /**
     *
     * @param battleState
     * @param target
     * @param isTeamOne
     * @return
     */
    private boolean isLegal(BattleState battleState, BattleMinionIdentifier target, boolean isTeamOne){
        BattleAvatar avatar;

        if(isTeamOne){
            if((avatar = battleState.getTeamTwo().get(target.getAvatarKey())) != null) {
                if (avatar.getBattleMinions().get(target.getMinionKey()) != null) {
                    if(avatar.getBattleMinions().get(target.getMinionKey()).isAlive()) {
                        return true;
                    }
                }
            }
            return false;
        } else {
            if ((avatar = battleState.getTeamOne().get(target.getAvatarKey())) != null) {
                if (avatar.getBattleMinions().get(target.getMinionKey()) != null) {
                    if(avatar.getBattleMinions().get(target.getMinionKey()).isAlive()) {
                        return true;
                    }
                }
            }
            return false;
        }
    }
}
