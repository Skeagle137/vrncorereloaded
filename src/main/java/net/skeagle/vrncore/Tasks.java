package net.skeagle.vrncore;

import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.rewards.RewardManager;
import net.skeagle.vrncore.rewards.TimedRewards;
import net.skeagle.vrncore.trail.VRNParticle;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrncore.utils.VRNPlayer;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class Tasks {

    public Tasks() {
        loadTasks();
    }

    public void loadTasks() {
        Task.asyncRepeating(() -> VRNcore.getInstance().getPlayerManager().save(), 0, 20 * (60 * 10));

        Task.syncRepeating(() -> {
            VRNPlayer vrnPlayer;
            for (Player pl : Bukkit.getOnlinePlayers()) {
                vrnPlayer = new VRNPlayer(pl);
                if (vrnPlayer.getPlayerTrail() != null) {
                    Particle particle = vrnPlayer.getPlayerTrail();
                    String perm = VRNParticle.getNameFromParticle(particle);
                    if (perm != null) {
                        if (pl.hasPermission("vrn.playertrails." + perm)) {
                            if (vrnPlayer.isVanished())
                                pl.spawnParticle(particle, pl.getLocation().clone().add(0, 0.35, 0),
                                        3, 0.3D, 0.3D, 0.3D, 0, particle == Particle.REDSTONE ? new Particle.DustOptions(Color.RED, 1.0f) : null);
                            else
                                pl.getLocation().getWorld().spawnParticle(particle, pl.getLocation().clone().add(0, 0.35, 0),
                                        3, 0.3D, 0.3D, 0.3D, 0, particle == Particle.REDSTONE ? new Particle.DustOptions(Color.RED, 1.0f) : null);
                        } else
                            vrnPlayer.setPlayerTrail(null);
                    }
                }
            }
        }, 0, 3);

        Task.asyncRepeating(() -> {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                AFKManager manager = AFKManager.getAfkManager(pl);
                VRNPlayer p = new VRNPlayer(pl);
                TimedRewards reward;
                long time = p.getTimePlayed();
                if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                    time += 1;
                    p.setTimePlayed(time);
                    reward = RewardManager.getInstance().getReward(String.valueOf(time));
                    if (reward != null) {
                        if (reward.checkPerm(pl))
                            Task.asyncDelayed(() -> reward.doReward(pl));
                    }
                    if (manager.getTimeAfk() > Settings.Afk.afktime)
                        manager.setAfk(true);
                } else if (manager.getTimeAfk() >= Settings.Afk.kickTime)
                    Task.syncDelayed(() -> pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.Afk.kickTime))));
            }
        }, 0, 20);
    }

    private boolean updateAFKPlayer(Player p) {
        AFKManager manager = AFKManager.getAfkManager(p);
        AFKManager.SavedLocation oldLoc = manager.getSavedLocation();
        AFKManager.SavedLocation loc = new AFKManager.SavedLocation(p);
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
