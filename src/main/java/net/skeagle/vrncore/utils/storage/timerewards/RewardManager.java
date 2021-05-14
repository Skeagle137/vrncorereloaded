package net.skeagle.vrncore.utils.storage.timerewards;

import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class RewardManager {

    private static final RewardManager instance = new RewardManager();

    public static RewardManager getInstance() {
        return instance;
    }

    private final List<TimeRewards> rewards = new ArrayList<>();

    public TimeRewards getReward(final String time) {
        for (final TimeRewards reward : rewards)
            if (reward.getName().equals(time))
                return reward;
        return null;
    }

    public void loadRewards() {
        rewards.clear();

        for (final File file : FileUtil.getFiles("rewards", "yml")) {
            final TimeRewards reward = new TimeRewards(file.getName().replace(".yml", "")); // You can also remove the extension here directly

            rewards.add(reward);
        }
    }
}
