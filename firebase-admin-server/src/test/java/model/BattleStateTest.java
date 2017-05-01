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
    public void doMove() throws Exception {

        Map<String, BattleAvatar> teamOne = new HashMap<>();
        Map<String, BattleAvatar> teamTwo = new HashMap<>();

        EMinionTemplate attackerEMinion = new EMinionTemplate(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");
        EMinionTemplate targetEMinion = new EMinionTemplate(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");

        BattleAvatar avatar1 = new BattleAvatar(attackerEMinion);
        BattleAvatar avatar2 = new BattleAvatar(targetEMinion);

        teamOne.put("avatar1", avatar1);
        teamTwo.put("avatar2", avatar2);

        BattleState battleState = new BattleState(teamOne, teamTwo);

        BattleMinionIdentifier attackerId = new BattleMinionIdentifier("avatar1", "minion-1");
        BattleMinionIdentifier targetId = new BattleMinionIdentifier("avatar2", "minion-1");

        BattleMinionIdentifier unknownIdentifier = new BattleMinionIdentifier("avatar3", "minion-1");

        battleState.performAndReportMove(attackerId, targetId);
        battleState.performAndReportMove(targetId, attackerId);

        battleState.performAndReportMove(unknownIdentifier, unknownIdentifier);
        battleState.performAndReportMove(unknownIdentifier, targetId);
        battleState.performAndReportMove(attackerId, unknownIdentifier);
        battleState.performAndReportMove(attackerId, attackerId);

        assertTrue(battleState.getTeamOne().get("avatar1").getBattleMinions().get("minion-1").battleStats.getCurrentHP() == 900);
        assertTrue(battleState.getTeamTwo().get("avatar2").getBattleMinions().get("minion-1").battleStats.getCurrentHP() == 900);
    }

    @Test
    public void advanceKill() throws Exception {
        // Player team
        final Map<String, BattleAvatar> playerTeam = new HashMap<>();
        final BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.addMinion("pMinion1", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
        playerTeam.put("uid1", playerAvatar);

        // E team
        final Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(new EMinion(57.0, 10.0, 3, "name", 1, 5, 1, 1, "Melee")));

        final BattleState state = new BattleState(playerTeam, eTeam);

        // player minion attacks e minion
        final Map<String, ChosenMove> playerMoves = new HashMap<>();
        playerMoves.put("pMinion1", new ChosenMove("eKey1", "minion-0", null));
        final Map<String, Map<String, ChosenMove>> chosenMoves = new HashMap<>();
        chosenMoves.put("uid1", playerMoves);

        // Advance
        state.advance(chosenMoves);

        // e minion 0, and only that should die
        assertTrue(state.getTeamOne().get("uid1").getBattleMinions().get("pMinion1").isAlive());
        assertFalse(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-0").isAlive());
        assertTrue(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-1").isAlive());
        assertTrue(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-2").isAlive());
    }

    @Test
    public void advanceNotKill() throws Exception {
        // Player team
        final Map<String, BattleAvatar> playerTeam = new HashMap<>();
        final BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.addMinion("pMinion1", new PlayerMinion("name1", 1, 6, 1, 1, "Melee"));
        playerTeam.put("uid1", playerAvatar);

        // E team
        final Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(new EMinion(57.0, 10.0, 3, "name", 2, 5, 1, 1, "Melee")));

        final BattleState state = new BattleState(playerTeam, eTeam);

        // player minion attacks e minion
        final Map<String, ChosenMove> playerMoves = new HashMap<>();
        playerMoves.put("pMinion1", new ChosenMove("eKey1", "minion-0", null));
        final Map<String, Map<String, ChosenMove>> chosenMoves = new HashMap<>();
        chosenMoves.put("uid1", playerMoves);

        // Advance
        state.advance(chosenMoves);

        // All should be alive
        assertTrue(state.getTeamOne().get("uid1").getBattleMinions().get("pMinion1").isAlive());
        assertTrue(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-0").isAlive());
        assertTrue(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-1").isAlive());
        assertTrue(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-2").isAlive());

        // minion-0 should have 1 health
        assertEquals(1, state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-0").getBattleStats().getCurrentHP());
    }

}