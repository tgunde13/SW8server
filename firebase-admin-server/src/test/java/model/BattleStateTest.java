package model;

import battle.ChosenMove;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class BattleStateTest {
    @Test
    public void advance() throws Exception {

        Map<String, BattleAvatar> playerTeam = new HashMap<>();
        BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.getBattleMinions().put("pMinion1", new Minion("name1", 1, 6, 1, 1, "Melee"));
        playerTeam.put("uid1", playerAvatar);

        Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(
                new EMinion(57.0, 10.0, 3, "name", 1, 5, 1, 1, "Melee")));

        final BattleState state = new BattleState(playerTeam, eTeam);

        Map<String, Map<String, ChosenMove>> chosenMoves = new HashMap<>();
        Map<String, ChosenMove> playerMoves = new HashMap<>();
        playerMoves.put()
        chosenMoves.put("uid1", playerMoves);

        state.advance(chosenMoves);
    }

}