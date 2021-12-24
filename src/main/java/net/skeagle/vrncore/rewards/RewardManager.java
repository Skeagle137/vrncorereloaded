package net.skeagle.vrncore.rewards;


import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrnlib.config.ConfigManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.logging.Level;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.log;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class RewardManager {

    private final ConfigManager rewardConfig;
    private static List<Reward> rewards = new ArrayList<>();

    public RewardManager(Plugin plugin) {
        rewards.add(new Reward("default"));
        rewardConfig = ConfigManager.create(plugin, "rewards.yml").target(this.getClass()).saveDefaults().load();
        this.checkLuckperms();
        this.save();
    }

    public void save() {
        rewardConfig.save();
    }

    public void reload() {
        rewardConfig.reload();
        this.checkLuckperms();
    }

    private void checkLuckperms() {
        if (!HookManager.isLuckPermsLoaded()) {
            Predicate<Reward> actionFound = r -> r.action != null;
            if (rewards.stream().anyMatch(actionFound)) {
                log(Level.SEVERE, color("&cRewards were found with an action, but luckperms is not installed. These rewards have been disabled."));
                rewards.removeIf(actionFound);
            }
        }
    }

    public void run(final Reward reward, final Player player) {
        if (reward.message != null) {
            sayNoPrefix(player, color(reward.replaceVars(player, reward.message)));
        }
        if (reward.title != null || reward.subtitle != null)
            player.sendTitle(reward.title != null ? color(reward.replaceVars(player, reward.title)) :
                    "", reward.subtitle != null ? color(reward.replaceVars(player, reward.subtitle)) : "", 5, 120, 40);
        Task.syncDelayed(() -> reward.sendFirework(player.getLocation().add(0, 1.5, 0)));
        if (reward.action != null && reward.group != null)
            reward.action.get().accept(player, reward.group);
    }

    public Reward getRewardByTime(final long l) {
        return rewards.stream().filter(r -> r.getTime() == l).findFirst().orElse(null);
    }
}
