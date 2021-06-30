package net.skeagle.vrncore;

import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.rewards.Reward;
import net.skeagle.vrncore.trail.Trail;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrncore.utils.VRNPlayer;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.Locale;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class Tasks {

    public Tasks() {
        loadTasks();
    }

    public void loadTasks() {
        Task.asyncRepeating(() -> {
            VRNcore.getInstance().getPlayerManager().save();
            VRNcore.getInstance().getRewardManager().getRewards().forEach(Reward::save);
        }, 0, 20L * (Settings.autoSaveInterval * 60L));

        Task.syncRepeating(() -> {
            VRNPlayer vrnPlayer;
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                vrnPlayer = new VRNPlayer(pl);
                if (vrnPlayer.getPlayerTrail() == null) {
                    continue;
                }
                final Trail trail = Trail.getFromParticle(vrnPlayer.getPlayerTrail());
                if (trail != null) {
                    if (pl.hasPermission("vrn.playertrails." + trail.toString().toLowerCase(Locale.ROOT)))
                        trail.run(pl, pl.getLocation().clone().add(0, 0.35, 0), 3, 0.2D, 0, vrnPlayer.isVanished());
                    else
                        vrnPlayer.setPlayerTrail(null);
                }
            }
        }, 0, 3);

        Task.asyncRepeating(() -> {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                final AFKManager manager = AFKManager.getAfkManager(pl);
                final VRNPlayer p = new VRNPlayer(pl);
                final Reward reward;
                long time = p.getTimePlayed();
                if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                    time += 1;
                    p.setTimePlayed(time);
                    reward = VRNcore.getInstance().getRewardManager().getRewardByTime(time);
                    if (reward != null) {
                        if (reward.checkPerm(pl))
                            Task.asyncDelayed(() -> reward.run(pl));
                    }
                    if (manager.getTimeAfk() > Settings.Afk.afktime)
                        manager.setAfk(true);
                } else if (manager.getTimeAfk() >= Settings.Afk.kickTime)
                    Task.syncDelayed(() -> pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.Afk.kickTime))));
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
