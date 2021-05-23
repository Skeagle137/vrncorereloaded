package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.timerewards.RewardManager;
import net.skeagle.vrncore.timerewards.TimeRewards;
import net.skeagle.vrncore.trail.VRNParticle;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class TaskSetup {

    static {
        Task.asyncRepeating(PlayerManager::save, 0, 20 * (60 * 10));

        Task.syncRepeating(() -> {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (!PlayerManager.getData(pl.getUniqueId()).isVanished()) {
                    if (PlayerManager.getData(pl.getUniqueId()).getPlayertrail() != null) {
                        final Particle particle = PlayerManager.getData(pl.getUniqueId()).getPlayertrail();
                        final String perm = VRNParticle.getNameFromParticle(particle);
                        if (perm != null) {
                            if (pl.hasPermission("vrn.playertrails." + perm)) {
                                if (particle != Particle.REDSTONE)
                                    pl.getLocation().getWorld().spawnParticle(particle, pl.getLocation().clone().add(0, 0.35, 0), 3, 0.3D, 0.3D, 0.3D, 0);
                                else
                                    spawnRedstone(pl.getLocation(), 3);
                            } else
                                PlayerManager.getData(pl.getUniqueId()).setPlayertrail(null);
                        }
                    }
                }
            }
        }, 0, 3);

        Task.asyncRepeating(() -> {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                final AFKManager manager = AFKManager.getAfkManager(pl);
                final VRNPlayer p = new VRNPlayer(pl);
                final TimeRewards reward;
                long time = p.getTimePlayed();
                if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                    reward = RewardManager.getInstance().getReward(String.valueOf(time));
                    if (reward != null)
                        if (reward.checkPerm(pl))
                            Common.runAsync(() -> reward.doReward(pl));
                    time += 1;
                    p.setTimePlayed(time);
                    if (manager.getTimeAfk() > Settings.Afk.STOP_COUNTING)
                        manager.setAfk(true);
                } else if (manager.getTimeAfk() >= Settings.Afk.KICK_TIME_IN_SECONDS)
                    Common.runLater(() -> pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.Afk.KICK_TIME_IN_SECONDS))));
            }
        }, 0, 20);
    }

    private static void spawnRedstone(final Location location, final int amount) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(0, 0.35, 0), amount, 0.3D, 0.3D, 0.3D, 0, new Particle.DustOptions(Color.RED, 1.0f));
    }

    private static boolean updateAFKPlayer(final Player p) {
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
