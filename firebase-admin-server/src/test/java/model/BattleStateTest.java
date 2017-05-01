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

        EMinion attackerEMinion = new EMinion(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");
        EMinion targetEMinion = new EMinion(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");

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

        assertTrue(battleState.getTeamOne().get("avatar1").getBattleMinions().get("minion-1").health == 900);
        assertTrue(battleState.getTeamTwo().get("avatar2").getBattleMinions().get("minion-1").health == 900);
    }

    private PlayerMinion minion1 = new PlayerMinion("name1", 1, 6, 1, 1, "Melee");
    private EMinion slowEMinion = new EMinion(57.0, 10.0, 3, "name", 1, 5, 1, 1, "Melee");

    @Test
    public void advance() throws Exception {

        Map<String, BattleAvatar> playerTeam = new HashMap<>();
        BattleAvatar playerAvatar = new BattleAvatar();
        playerAvatar.addMinion("pMinion1", minion1);
        playerTeam.put("uid1", playerAvatar);

        Map<String, BattleAvatar> eTeam = new HashMap<>();
        eTeam.put("eKey1", new BattleAvatar(slowEMinion));

        final BattleState state = new BattleState(playerTeam, eTeam);

        Map<String, ChosenMove> playerMoves = new HashMap<>();
        playerMoves.put("pMinion1", new ChosenMove("eKey1", "minion-0", null));

        Map<String, Map<String, ChosenMove>> chosenMoves = new HashMap<>();
        chosenMoves.put("uid1", playerMoves);

        // Advance
        state.advance(chosenMoves);

        // e minion 0, and only that should die
        assertTrue(state.getTeamTwo().get("eKey1").hasAliveMinions());
        assertFalse(state.getTeamTwo().get("eKey1").getBattleMinions().get("minion-0").isAlive());
    }

}