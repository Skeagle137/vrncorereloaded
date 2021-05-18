package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrncore.utils.storage.timerewards.RewardManager;
import net.skeagle.vrncore.utils.storage.timerewards.TimeRewards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.Common;

import static net.skeagle.vrncore.api.util.VRNUtil.color;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class PlayerUpdateTask extends BukkitRunnable {

    public PlayerUpdateTask(final VRNcore vrn) {
        runTaskTimerAsynchronously(vrn, 0, 20);
    }

    @Override
    public void run() {
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
