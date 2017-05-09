package battle;

import model.BattleAvatar;
import model.BattleState;
import model.EMinionTemplate;
import model.PlayerMinion;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;

/**
 *
 */
public class EnvironmentMovesHandlerTest {
    @Test
    public void addMoves() throws Exception {
        // Player team
        final Map<String, BattleAvatar> playerTeam = new HashMap<>();
        final BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.addMinion("pMinion1", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
        playerTeam.put("uid1", playerAvatar);

        // E team
        final Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(new EMinionTemplate(57.0, 10.0, 3, "name", 2, 5, 1, 1, "Melee")));

        final BattleState state = new BattleState(playerTeam, eTeam);

        final AvatarChoices choices = new AvatarChoices(new HashMap<>());

        assertEquals(0, choices.getMoves().size());

        EnvironmentMovesHandler.addMoves(state, choices);

        assertEquals(1, choices.getMoves().size());
    }

}