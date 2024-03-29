package model;

import com.google.firebase.database.Exclude;

import java.util.*;


/**
 * Class representing a battle avatar which is an avatar used during battles. The avatar has a list of minions being used
 * and a possible userId if the battle avatar is being controlled by a player
 */
public class BattleAvatar {
    private final Map<String,Minion> battleMinions;
    private final boolean isPlayerControlled;

    /**
     * Constructor for creating a battle avatar with a list of minions and a user ID.
     */
    public BattleAvatar(){
        battleMinions = new HashMap<>();
        isPlayerControlled = true;
    }

    /**
     * Constructor for a battle avatar that does not belong to a player but an environment minion.
     * Creates a number of environment minions equal to the size of the environment minion
     * @param eMinion is the environment minion that is being fought.
     */
    public BattleAvatar(final EMinionTemplate eMinion){
        isPlayerControlled = false;
        battleMinions = new HashMap<>();
        for (int i = 0; i < eMinion.getSize(); i++){
            final Minion eMinionToPut = eMinion.createMinion();
            eMinionToPut.battleStats = new BattleStats(eMinionToPut);
            eMinionToPut.assignTypeClass();
            battleMinions.put("minion-" + i, eMinionToPut);
        }
    }


    /**
     * Getter for BattleMinions
     * @return battleMinions
     */
    public Map<String,Minion> getBattleMinions(){ return battleMinions; }

    /**
     *
     * @return true if battle avatar is controlled by the player.
     *         False if it is controlled by the server.
     */
    @Exclude
    public boolean isPlayerControlled() {
        return isPlayerControlled;
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

    /**
     * Adds a minion to the map of minions
     * @param key of the minion that is being added
     * @param minion that is being added
     */
    public void addMinion(final String key, final PlayerMinion minion){
        minion.createBattleStats();
        minion.assignTypeClass();
        battleMinions.put(key, minion);
    }
}
