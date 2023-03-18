package net.skeagle.vrncore;

import net.skeagle.vrncore.commands.rewards.Reward;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
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
                    if (!this.checkAFK(pl) || !manager.isAfk()) {
                        time++;
                        data.setTimePlayed(time);
                        final Reward reward = plugin.getRewardManager().getRewardByTime(time);
                        if (reward != null) {
                            if (reward.checkPerm(pl)) {
                                Task.asyncDelayed(() -> plugin.getRewardManager().run(reward, pl));
                                data.updateName();
                            }
                        }
                        if (manager.getTimeAfk() >= Settings.afktime) {
                            manager.setAfk(true);
                        }
                    } else if (manager.getTimeAfk() >= Settings.kickTime && !pl.hasPermission("vrn.afkexempt")) {
                        Task.syncDelayed(() -> pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.kickTime))));
                        manager.remove(pl);
                    }
                });
            }
        }, 0, 20);
    }

    private boolean checkAFK(final Player player) {
        final AFKManager manager = AFKManager.getAfkManager(player);
        Location old = manager.getSavedLocation();
        Location current = player.getLocation();
        manager.setSavedLocation(current);
        if (old != null && (old.getX() == current.getX() && old.getY() == current.getY() && old.getZ() == current.getZ())) {
            manager.decrementIdleCountdown();
        }
        if (old == null || old.getYaw() != current.getYaw() && old.getPitch() != current.getPitch()) {
            manager.setTimeAfk(0);
            if (manager.isAfk()) {
                manager.setAfk(false);
            }
        }
        manager.setTimeAfk(manager.getTimeAfk() + 1);
        return true;
    }
}
