package model;

/**
 * Represents a player in our system
 */
public class Player {
    private String name;
    private final int gold = 0;

    /**
     * Constructor to create a player
     * @param name of the player
     */
    public Player(final String name) {
        this.name = name;
    }

    /**
     * Dafault constructor
     */
    private Player() {}

    /**
     * Getter for the name
     * @return the name of the player
     */
    public String getName() {
        return name;
    }

    /**
     * Getter for gold
     * @return the amount of gold on the player
     */
    public int getGold() {return gold;}
}
