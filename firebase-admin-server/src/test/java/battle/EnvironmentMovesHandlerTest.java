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
    public void addMoves1Opponent() throws Exception {
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
        assertEquals(3, choices.getMoves().get("eKey1").size());

        for (ChosenMove move : choices.getMoves().get("eKey1").values()) {
            assertEquals("uid1", move.getAvatarKey());
            assertEquals("pMinion1", move.getMinionKey());
        }
    }

    @Test
    public void randomMoves() throws Exception {
        int minion1 = 0;
        int minion2 = 0;

        for(int i = 0; i < 100; i++) {

            // Player team
            final Map<String, BattleAvatar> playerTeam = new HashMap<>();
            final BattleAvatar playerAvatar = new BattleAvatar();
            playerAvatar.addMinion("pMinion1", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
            playerAvatar.addMinion("pMinion2", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
            playerTeam.put("uid1", playerAvatar);

            // E team
            final Map<String, BattleAvatar> eTeam = new HashMap<>();
            eTeam.put("eKey1", new BattleAvatar(new EMinionTemplate(57.0, 10.0, 1, "name", 2, 5, 1, 1, "Melee")));

            final BattleState state = new BattleState(playerTeam, eTeam);

            final AvatarChoices choices = new AvatarChoices(new HashMap<>());

            EnvironmentMovesHandler.addMoves(state, choices);

            if(choices.getMoves().get("eKey1").get("minion-0").getMinionKey().equals("pMinion1")){
                minion1++;
            } else {
                minion2++;
            }
        }

        System.out.println(minion1 + " " + minion2);
    }

    @Test
    public void addMoves2Opponents() throws Exception {
        // Player team
        final Map<String, BattleAvatar> playerTeam = new HashMap<>();
        final BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.addMinion("pMinion1", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
        playerAvatar.addMinion("pMinion2", new PlayerMinion("name2", 1, 6, 1, 1, "Melee"));
        playerTeam.put("uid1", playerAvatar);

        // E team
        final Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(new EMinionTemplate(57.0, 10.0, 3, "name", 2, 5, 1, 1, "Melee")));

        final BattleState state = new BattleState(playerTeam, eTeam);

        final AvatarChoices choices = new AvatarChoices(new HashMap<>());

        assertEquals(0, choices.getMoves().size());

        EnvironmentMovesHandler.addMoves(state, choices);

        assertEquals(1, choices.getMoves().size());
        assertEquals(3, choices.getMoves().get("eKey1").size());
    }

}