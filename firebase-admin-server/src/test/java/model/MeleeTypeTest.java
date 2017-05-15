package model;

import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

/**
 * Created by Chres on 09-05-2017.
 */
public class MeleeTypeTest {
    @Test
    public void getAvailableMoves() throws Exception {
        Map<String, BattleAvatar> teamOne = new HashMap<>();
        Map<String, BattleAvatar> teamTwo = new HashMap<>();
        List<BattleMinionIdentifier> correctList = new ArrayList<>();

        EMinionTemplate attackerEMinion = new EMinionTemplate(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");
        EMinionTemplate targetEMinion = new EMinionTemplate(57.0, 10.0, 3, "name", 1000, 5, 100, 1, "Melee");

        BattleAvatar avatar1 = new BattleAvatar(attackerEMinion);
        BattleAvatar avatar2 = new BattleAvatar(targetEMinion);

        teamOne.put("avatar1", avatar1);
        teamTwo.put("avatar2", avatar2);

        BattleState battleState = new BattleState(teamOne, teamTwo);

        BattleMinionIdentifier attackerId = new BattleMinionIdentifier("avatar1", "minion-" + 1);
        BattleMinionIdentifier targetId0 = new BattleMinionIdentifier("avatar2", "minion-" + 0);
        BattleMinionIdentifier targetId1 = new BattleMinionIdentifier("avatar2", "minion-" + 1);
        BattleMinionIdentifier targetId2 = new BattleMinionIdentifier("avatar2", "minion-" + 2);

        MeleeType type = new MeleeType();

        correctList.add(targetId2);
        correctList.add(targetId1);
        correctList.add(targetId0);

        assertTrue(type.getAvailableMoves(battleState, attackerId).containsAll(correctList));
    }

}