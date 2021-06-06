package net.skeagle.vrncore.rewards;

import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RewardManager {

    private static final RewardManager instance = new RewardManager();

    public static RewardManager getInstance() {
        return instance;
    }

    private final List<TimedRewards> rewards = new ArrayList<>();

    public TimedRewards getReward(final String time) {
        for (final TimedRewards reward : rewards)
            if (reward.getName().equals(time))
                return reward;
        return null;
    }

    public void loadRewards() {
        rewards.clear();

        for (final File file : FileUtil.getFiles("rewards", "yml")) {
            final TimedRewards reward = new TimedRewards(file.getName().replace(".yml", ""));
            rewards.add(reward);
        }
    }
}
