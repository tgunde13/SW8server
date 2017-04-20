package model;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;


/**
 * Class representing a battle avatar which is an avatar used during battles. The avatar has a list of minions being used
 * and a possible userId if the battle avatar is being controlled by a player
 */
public class BattleAvatar {
    private List<Minion> battleMinions;
    private String userId;

    /**
     * Constructor for creating a battle avatar with a list of minions and a user ID.
     * @param minions list of minions being used by the battle avatar.
     * @param UserId Id of the user this avatar belongs to.
     */
    public BattleAvatar(final List<PlayerMinion> minions, final String UserId){
        battleMinions = new ArrayList<>();

        this.userId = UserId;

        for(final Minion minion : minions){
            minion.battleStats = new BattleStats(minion);
            battleMinions.add(minion);
        }
    }

    /**
     * Constructor for a battle avatar that does not belong to a player but an environment minion.
     * @param eMinion is the environment minion that is being fought.
     */
    public BattleAvatar(final EMinion eMinion){
        battleMinions = new ArrayList<>();
        eMinion.battleStats = new BattleStats(eMinion);
        battleMinions.add(eMinion);
    }


    /**
     * Default constructor, used by firebase
     */
    private BattleAvatar(){
    }


    /**
     * Getter for battleminions
     * @return battleMinions
     */
    public List<Minion> getBattleMinions(){ return battleMinions; }


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
}
