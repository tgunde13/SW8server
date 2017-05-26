package model;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by Chres on 26-05-2017.
 */
public class PlayerMinionTest {
    @Test
    public void getXp() throws Exception {
        Minion minion = new Minion("testMinion", 1, 1, 1, 1, "test");
        PlayerMinion playerMinion = new PlayerMinion(minion);
        assertTrue(playerMinion.getXp() == 0);
    }

    @Test
    public void addXP() throws Exception {
        PlayerMinion playerMinion = new PlayerMinion("testMinion", 1, 1, 1, 1, "test");
        playerMinion.addXP(11);
        assertTrue(playerMinion.getLevel() == 2);
        assertTrue(playerMinion.getXp() == 1);

        playerMinion.addXP(11);
        assertTrue(playerMinion.getLevel() == 2);
        assertTrue(playerMinion.getXp() == 12);
    }

    @Test
    public void levelUp(){
        PlayerMinion playerMinionSwordman = new PlayerMinion("Swordman", 1, 1, 1, 1, "Melee");
        playerMinionSwordman.addXP(11);
        PlayerMinion playerMinionSpearman= new PlayerMinion("Spearman", 1, 1, 1, 1, "Melee");
        playerMinionSpearman.addXP(11);
        PlayerMinion playerMinionMaxLevel = new PlayerMinion("Swordman", 1, 1, 1, 30, "Melee");
        playerMinionMaxLevel.addXP(11);

        assertTrue(playerMinionSwordman.getHealth() == 101);
        assertTrue(playerMinionSwordman.getPower() == 36);
        assertTrue(playerMinionSpearman.getHealth() == 201);
        assertTrue(playerMinionSpearman.getPower() == 21);
        assertTrue(playerMinionMaxLevel.getLevel() == 30);
    }

}