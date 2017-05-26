package model;

/**
 * Represents a minion owned by a player
 */
public class PlayerMinion extends Minion {
    private int xp = 0;

    /**
     * Constructor for a PlayerMinion
     * @param name of the minion
     * @param health of the minion
     * @param speed of the minion
     * @param power of the minion
     * @param level of the minion
     * @param type of minion
     */
    public PlayerMinion(final String name, final int health, final int speed, final int power, final int level, final String type){
        super(name, health, speed, power, level, type);
    }

    /**
     * Constructor to convert a minion to a player minion
     * @param minion takes a minion
     */
    public PlayerMinion(final Minion minion) {
        super(minion.getName(), minion.getHealth(), minion.getSpeed(), minion.getPower(), minion.getLevel(), minion.getType());
    }

    /**
     * Default constructor
     */
    private PlayerMinion() {}

    /**
     * Getter for xp
     * @return xp
     */
    @SuppressWarnings("unused")
    public int getXp() {
        return xp;
    }

    /**
     * Adds xp and calls level up if the xp exceeds a certain threshold
     * @param xpToAdd xp to add to the current xp
     */
    public void addXP(final int xpToAdd) {
        xp += xpToAdd;
        while(xp > level * 10){
            levelUp();
        }
    }

    /**
     * levels up the minion
     */
    private void levelUp(){
        if(level == MAX_LEVEL){
         xp = level * 10;
        }
        xp = xp-(level * 10);
        level++;
        if(name.equals("Swordman")){
            health += 100;
            power += 35;
        } else if (name.equals("Spearman")){
            health += 200;
            power += 20;
        }
    }
}
