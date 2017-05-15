package model;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Melee type for minions
 */
public class MeleeType extends Type {

    /**
     * Default constructor
     */
    public MeleeType(){}


    /**
     * Method for calculating a move based on this type
     * @param battleState the state the move is performed in
     * @param target the target of the move
     * @param attacker the minion performing the move
     * @param isTeamOne a value to determine if the attacker is on team one, makes searching easier
     * @return the calculated move, however the move is not applied yet
     */
    public BattleMove calculateMove(final BattleState battleState, final BattleMinionIdentifier attacker, final BattleMinionIdentifier target, final boolean isTeamOne){
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
    public List<BattleMinionIdentifier> getAvailableMoves(final BattleState state, final BattleMinionIdentifier attacker){
        final List<BattleMinionIdentifier> availableMoves = new ArrayList<>();
        if(!(state.getTeamOne().get(attacker.getAvatarKey()) == null)){
            for(final Map.Entry<String, BattleAvatar> avatarEntry: state.getTeamTwo().entrySet()){
                for(final Map.Entry<String, Minion> minionEntry : avatarEntry.getValue().getBattleMinions().entrySet()){
                    if(minionEntry.getValue().isAlive()){
                        availableMoves.add(new BattleMinionIdentifier(avatarEntry.getKey(), minionEntry.getKey()));
                    }
                }
            }
        } else {
            for(final Map.Entry<String, BattleAvatar> avatarEntry: state.getTeamOne().entrySet()){
                for(final Map.Entry<String, Minion> minionEntry : avatarEntry.getValue().getBattleMinions().entrySet()){
                    if(minionEntry.getValue().isAlive()){
                        availableMoves.add(new BattleMinionIdentifier(avatarEntry.getKey(), minionEntry.getKey()));
                    }
                }
            }
        }
        return availableMoves;
    }

    /**
     * Determines if a target is legal
     * @param battleState the state the move is made in
     * @param target the target of the move
     * @param isTeamOne value to determine if attacker is on team one
     * @return true if the target is a valid target for the attacker
     */
    private boolean isLegal(final BattleState battleState, final BattleMinionIdentifier target, final boolean isTeamOne){
        final BattleAvatar avatar;

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
