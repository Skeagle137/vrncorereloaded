package net.skeagle.vrncore.utils.storage.timerewards;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class RewardManager {

    @Getter
    private static final RewardManager instance = new RewardManager();

    @Getter
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
