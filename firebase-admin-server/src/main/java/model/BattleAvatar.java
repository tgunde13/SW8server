package model;

import com.google.firebase.database.Exclude;

import java.util.*;


/**
 * Class representing a battle avatar which is an avatar used during battles. The avatar has a list of minions being used
 * and a possible userId if the battle avatar is being controlled by a player
 */
public class BattleAvatar {
    private Map<String,Minion> battleMinions;
    private String userId;

    /**
     * Constructor for creating a battle avatar with a list of minions and a user ID.
     * @param UserId Id of the user this avatar belongs to.
     */
    public BattleAvatar(){
        battleMinions = new HashMap<>();
    }

    /**
     * Constructor for a battle avatar that does not belong to a player but an environment minion.
     * @param eMinion is the environment minion that is being fought.
     */
    public BattleAvatar(final EMinion eMinion){
        battleMinions = new HashMap<>();
        for (int i = 0; i < eMinion.getSize(); i++){
            Minion eMinionToPut = eMinion;
            eMinionToPut.battleStats = new BattleStats(eMinionToPut);
            battleMinions.put("minion-" + i, eMinionToPut);
        }
    }


    /**
     * Getter for battleminions
     * @return battleMinions
     */
    public Map<String,Minion> getBattleMinions(){ return battleMinions; }


    /**
     * Getter for userId
     * @return userId
     */
    public String getUserId() { return userId; }

    /**
     *
     * @return true if battle avatar is controlled by the player.
     *         False if it is controlled by the server.
     */
    @Exclude
    public boolean isPlayerControlled() {
        return userId != null;
    }

    /**
     * Gets if this has alive minions.
     * The minions should have battle stats before calling this.
     * @return true if, and only if, this has alive minions
     */
    boolean hasAliveMinions() {
        for (final Minion minion : battleMinions.values()) {
            if (minion.isAlive()) {
                return true;
            }
        }

        return false;
    }
}
