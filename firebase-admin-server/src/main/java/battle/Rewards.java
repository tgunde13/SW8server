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
class Rewards {
    private static final int XP_MULTIPLIER = 10;
    final Map<String, Reward> rewards = new HashMap<>();

    /**
     * Constructor, takes a BattleState and from this it generates a Map consisting of rewards as values and userIds as keys
     * @param battleState The BattleState we want to calculate the rewards for, must have either status "teamOneWon" or "TeamTwoWon" to generate rewards
     */
    public Rewards(final BattleState battleState){
        int teamOneXpReward = 0;
        int teamTwoXpReward = 0;
        int teamOneGoldReward = 0;
        int teamTwoGoldReward = 0;

        Minion rewardMinion = null;

        final List<String> deadMinions = new ArrayList<>();
        final List<String> usedMinions = new ArrayList<>();

        if(battleState.getStatus().equals(BattleState.TEAM_ONE_WIN)){
            teamOneGoldReward = 2;
            teamTwoGoldReward = 1;
            for(final Map.Entry<String, BattleAvatar> entry : battleState.getTeamTwo().entrySet()){
                for (final Minion minion : entry.getValue().getBattleMinions().values()) {
                    teamOneXpReward += minion.getLevel() * XP_MULTIPLIER;

                    if (rewardMinion == null && !entry.getValue().isPlayerControlled()){
                        rewardMinion = minion;
                    }
                }
            }
        } else if(battleState.getStatus().equals(BattleState.TEAM_TWO_WIN)){
            teamOneGoldReward = 1;
            teamTwoGoldReward = 2;
            for(final Map.Entry<String, BattleAvatar> entry : battleState.getTeamOne().entrySet()){
                for (final Minion minion : entry.getValue().getBattleMinions().values()) {
                    teamTwoXpReward += minion.getLevel() * XP_MULTIPLIER;
                }
            }
        }

        for(final Map.Entry<String, BattleAvatar> entry : battleState.getTeamOne().entrySet()){
            for (final Map.Entry<String, Minion> minionEntry : entry.getValue().getBattleMinions().entrySet()) {
                if(!minionEntry.getValue().getBattleStats().isAlive()) {
                    deadMinions.add(minionEntry.getKey());
                } else {
                    usedMinions.add(minionEntry.getKey());
                }
            }
            rewards.put(entry.getKey(), new Reward(teamOneGoldReward, teamOneXpReward, rewardMinion, deadMinions, usedMinions));
        }
        for(final Map.Entry<String, BattleAvatar> entry : battleState.getTeamTwo().entrySet()){
            if(entry.getValue().isPlayerControlled()) {
                for (final Map.Entry<String, Minion> minionEntry : entry.getValue().getBattleMinions().entrySet()) {
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
