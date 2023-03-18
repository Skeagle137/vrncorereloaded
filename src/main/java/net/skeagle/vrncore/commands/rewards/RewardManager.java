package net.skeagle.vrncore.commands.rewards;


import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.SuperVanishHook;
import net.skeagle.vrnlib.config.ConfigManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.log;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class RewardManager {

    private ConfigManager rewardConfig;
    private static List<Reward> rewards = new ArrayList<>();

    public RewardManager(Plugin plugin) {
        if (!HookManager.isLuckPermsLoaded()) {
            log(Level.SEVERE, color("&cRewards require luckperms to be installed. Rewards will be disabled."));
            return;
        }
        rewardConfig = ConfigManager.create(plugin, "rewards.yml").target(this.getClass()).saveDefaults().load();
        if (rewards.isEmpty()) {
            rewards.add(new Reward("default"));
            this.save();
        }
    }

    public void save() {
        if (rewardConfig == null)
            return;
        rewardConfig.save();
    }

    public void reload() {
        if (rewardConfig == null)
            return;
        rewardConfig.reload();
    }

    public void run(final Reward reward, final Player player) {
        if (reward.message != null) {
            sayNoPrefix(player, color(reward.replaceVars(player, reward.message)));
        }
        if (reward.title != null || reward.subtitle != null)
            player.sendTitle(reward.title != null ? color(reward.replaceVars(player, reward.title)) :
                    "", reward.subtitle != null ? color(reward.replaceVars(player, reward.subtitle)) : "", 5, 120, 40);
        if (reward.action != null && reward.group != null)
            reward.action.get().accept(player, reward.group);
        if (HookManager.isSuperVanishLoaded() && SuperVanishHook.isVanished(player)) {
            return;
        }
        Task.syncDelayed(() -> reward.sendFirework(player.getLocation().add(0, 1.5, 0)));
    }

    public Reward getRewardByTime(final long l) {
        return rewards.stream().filter(r -> r.getTime() == l).findFirst().orElse(null);
    }
}
