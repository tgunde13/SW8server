package battle;

import model.Minion;

import java.util.ArrayList;
import java.util.List;

/**
 * A class representing a reward, has a gold value, xp value and a minion value
 */
class Reward {
    private int gold;
    private int xp;
    private Minion minion;
    private List<String> deadMinions = new ArrayList<>();
    private List<String> usedMinions = new ArrayList<>();

    /**
     * Constructor, creates a reward
     * @param gold the amount of gold to be awarded
     * @param xp the amount of xp to be awarded
     * @param minion the minion to be awarded
     * @param deadMinions a list of minions that have died
     * @param usedMinions a list of minions that are used but alive
     */
    public Reward(final int gold, final int xp, final Minion minion, final List<String> deadMinions, final List<String> usedMinions){
        this.gold = gold;
        this.xp = xp;
        this.minion = minion;
        this.deadMinions = deadMinions;
        this.usedMinions = usedMinions;
    }

    /**
     * Defualt constructor, for firebase
     */
    @SuppressWarnings("unused")
    private Reward(){}

    /**
     * Getter for gold
     * @return the amount of gold this reward awards
     */
    public int getGold() {return gold;}

    /**
     * Getter for xp
     * @return the amount of xp this reward awards
     */
    public int getXp() {return xp;}

    /**
     * Getter for minion
     * @return the minion this reward awards
     */
    public Minion getMinion() {return minion;}

    /**
     * Getter for dead minions
     * @return a list of minions that a user has lost
     */
    public List<String> getDeadMinions() {return deadMinions;}

    /**
     * Getter for used minions
     * @return a list of minions that a user have used but not lost
     */
    public List<String> getUsedMinions() {return usedMinions;}
}
