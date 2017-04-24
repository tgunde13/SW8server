package model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chres on 18-04-2017.
 */



public class MeleeType extends Type {

    MeleeType(){ id = "Melee"; }


    /**
     * Takes a move and a battleState and checks whether the move is legal in the given battleState
     * @param battleState the current battleState we want to check in
     * @param move battleMove that represents the move we want to check for
     * @return true if the move is legal or false if the move is illegal
     */
    public boolean isLegal(BattleState battleState,BattleMove move){
        String casterKey = move.getMinionKeys().get(0);
        move.getMinionKeys().remove(0);
        ArrayList<String> targetKeys = new ArrayList<>();
        targetKeys.addAll(move.getMinionKeys());

        Map<String, Minion> validTargets = targets(battleState, casterKey);

        for(String key: targetKeys){
            if((validTargets.getOrDefault(key, null)) == null) {
                return false;
            }
        }
        return true;
    }


    /**
     * Takes a minion key and a battleState and checks which minions are valid targets for the given minion in the battleState
     * @param battleState current battleState
     * @param key the minions key in its corresponding hashmap
     * @return a list of possible targets for the minion with the given key in the given battleState
     */
    private Map<String, Minion> targets(final BattleState battleState, String key){
        HashMap<String, Minion> targets = new HashMap<>();

        for (BattleAvatar battleavatar: battleState.getTeamOne()) {
            if (null != battleavatar.getBattleMinions().getOrDefault(key, null)){
                for (BattleAvatar battleAvatarTeamTwo: battleState.getTeamTwo()) {
                    targets.putAll(battleAvatarTeamTwo.getBattleMinions());
                }
            } else {
                for (BattleAvatar battleAvatarTeamOne: battleState.getTeamOne()) {
                    targets.putAll(battleAvatarTeamOne.getBattleMinions());
                }
            }
        }

        return targets;
    }
}
