package net.skeagle.vrncore.rewards;


import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;

import java.util.ArrayList;
import java.util.List;

public class RewardManager {

    private final List<Reward> rewards;

    public RewardManager() {
        rewards = new ArrayList<>();
        load();
    }

    public void load() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM rewards");
        res.forEach(reward -> {
            final String name = reward.getString(2);
            final boolean permission = reward.getBoolean(3);
            final String message = res.getString(4);
            final boolean firework = res.getBoolean(5);
            final String title = res.getString(6);
            final String subtitle = res.getString(7);
            final RewardType rewardType = RewardType.valueOf(res.getString(8));
            final RewardAction rewardAction = res.getString(9) == null ? null : RewardAction.valueOf(res.getString(9));
            final String group = res.getString(10);
            final long cost = res.getLong(11);
            final long time = res.getLong(12);
            rewards.add(new Reward(name, permission, message, firework, title, subtitle, rewardType, rewardAction, group, cost, time));
        });
    }

    public void createReward(final String name) {
        final Reward reward = new Reward(name);
        rewards.add(reward);
        Task.asyncDelayed(reward::save);
    }

    public void deleteReward(final Reward reward) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM rewards WHERE name = ?", reward.getName());
        rewards.remove(reward);
    }

    public Reward getReward(final String s) {
        return rewards.stream().filter(r -> r.getName().equalsIgnoreCase(s)).findFirst().orElse(null);
    }

    public Reward getRewardByTime(final long l) {
        return rewards.stream().filter(r -> r.getTime() == l).findFirst().orElse(null);
    }

    public List<Reward> getRewards() {
        return rewards;
    }
}
