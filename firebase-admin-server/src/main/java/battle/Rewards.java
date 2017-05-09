package battle;
import model.BattleAvatar;
import model.BattleState;
import model.Minion;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that calculates and stores rewards that are to be awarded.
 */
public class Rewards {
    private static final int XP_MULTIPLIER = 10;
    Map<String, Reward> rewards = new HashMap<>();

    /**
     * Constructor, takes a BattleState and from this it generates a Map consisting of rewards as values and userIds as keys
     * @param battleState The BattleState we want to calculate the rewards for, must have either status "teamOneWon" or "TeamTwoWon" to generate rewards
     */
    public Rewards(BattleState battleState){
        int teamOneXpReward = 0;
        int teamTwoXpReward = 0;
        int teamOneGoldReward = 0;
        int teamTwoGoldReward = 0;

        Minion rewardMinion = null;

        List<String> deadMinions = new ArrayList<>();
        List<String> usedMinions = new ArrayList<>();

        if(battleState.getStatus() == "teamOneWon"){
            teamOneGoldReward = 2;
            teamTwoGoldReward = 1;
            for(Map.Entry<String, BattleAvatar> entry : battleState.getTeamTwo().entrySet()){
                for (Minion minion : entry.getValue().getBattleMinions().values()) {
                    teamOneXpReward += minion.getLevel() * XP_MULTIPLIER;

                    if (minion == null && entry.getValue().isPlayerControlled()){
                        rewardMinion = minion;
                    }
                }
            }
        } else if(battleState.getStatus() == "teamTwoWon"){
            teamOneGoldReward = 1;
            teamTwoGoldReward = 2;
            for(Map.Entry<String, BattleAvatar> entry : battleState.getTeamOne().entrySet()){
                for (Minion minion : entry.getValue().getBattleMinions().values()) {
                    teamTwoXpReward += minion.getLevel() * XP_MULTIPLIER;
                }
            }
        }

        for(Map.Entry<String, BattleAvatar> entry : battleState.getTeamOne().entrySet()){
            for (Map.Entry<String, Minion> minionEntry : entry.getValue().getBattleMinions().entrySet()) {
                if(!minionEntry.getValue().getBattleStats().isAlive()) {
                    deadMinions.add(minionEntry.getKey());
                } else {
                    usedMinions.add(minionEntry.getKey());
                }
            }
            rewards.put(entry.getKey(), new Reward(teamOneGoldReward, teamOneXpReward, rewardMinion, deadMinions, usedMinions));
        }
        for(Map.Entry<String, BattleAvatar> entry : battleState.getTeamOne().entrySet()){
            if(entry.getValue().isPlayerControlled()) {
                for (Map.Entry<String, Minion> minionEntry : entry.getValue().getBattleMinions().entrySet()) {
                    if (!minionEntry.getValue().getBattleStats().isAlive()) {
                        deadMinions.add(minionEntry.getKey());
                    } else {
                        usedMinions.add(minionEntry.getKey());
                    }
                }
                rewards.put(entry.getKey(), new Reward(teamTwoGoldReward, teamTwoXpReward, null, deadMinions, usedMinions));
            }
        }
    }
}
