package model;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


public class PlayerTest {
    @Test
    public void getName() throws Exception {
        Player player = new Player("test1");

        assertTrue(player.getName().equals("test1"));
    }

    @Test
    public void getGold() throws Exception {
        Player player = new Player("test1");

        assertTrue(player.getGold() == 0);

    }
}
