package net.skeagle.vrncore;

import net.skeagle.vrncore.rewards.Reward;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class Tasks {

    public Tasks(VRNcore plugin) {
        Task.asyncRepeating(() -> plugin.getPlayerManager().save(), 20L, 20L * (Settings.autoSaveInterval * 60L));

        Task.asyncRepeating(() -> {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                final AFKManager manager = AFKManager.getAfkManager(pl);
                plugin.getPlayerManager().getData(pl.getUniqueId()).thenAccept(data -> {
                    long time = data.getTimePlayed();
                    if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                        time += 1;
                        data.setTimePlayed(time);
                        final Reward reward = plugin.getRewardManager().getRewardByTime(time);
                        if (reward != null) {
                            if (reward.checkPerm(pl)) {
                                Task.asyncDelayed(() -> plugin.getRewardManager().run(reward, pl));
                                data.updateName();
                            }
                        }
                        if (manager.getTimeAfk() > Settings.afktime)
                            manager.setAfk(true);
                    } else if (manager.getTimeAfk() >= Settings.kickTime && !pl.hasPermission("vrn.afkexempt")) {
                        Task.syncDelayed(() -> pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.kickTime))));
                        manager.remove(pl);
                    }
                });
            }
        }, 0, 20);
    }

    private boolean updateAFKPlayer(final Player p) {
        final AFKManager manager = AFKManager.getAfkManager(p);
        final AFKManager.SavedLocation oldLoc = manager.getSavedLocation();
        final AFKManager.SavedLocation loc = new AFKManager.SavedLocation(p);
        manager.setSavedLocation(loc);
        if (oldLoc == null || !manager.isYawEqual(oldLoc) && !manager.isPitchEqual(oldLoc)) {
            manager.setTimeAfk(0);
            if (manager.isAfk())
                manager.setAfk(false);
            return false;
        }
        manager.setTimeAfk(manager.getTimeAfk() + 1);
        return true;
    }
}
