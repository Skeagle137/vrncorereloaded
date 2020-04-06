package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrncore.utils.timerewards.RewardManager;
import net.skeagle.vrncore.utils.timerewards.TimeRewards;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import static net.skeagle.vrncore.utils.TimeUtil.timeToMessage;
import static net.skeagle.vrncore.utils.VRNUtil.color;

public class UpdatePlayerTask extends BukkitRunnable {

    @Override
    public void run() {
        for (final Player pl : Remain.getOnlinePlayers()) {
            final AFKManager manager = AFKManager.getAfkManager(pl);
            final PlayerCache cache = PlayerCache.getCache(pl);
            final TimeRewards reward;
            int time = cache.getTimeplayed().getTimeSeconds();
            if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                reward = RewardManager.getInstance().getReward(String.valueOf(time));
                if (reward != null)
                    if (reward.checkPerm(pl))
                        reward.doReward(pl);

                time += Settings.Afk.SECONDS_DELAY;
                cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(time));
                if (manager.getTimeAfk() > Settings.Afk.STOP_COUNTING) {
                    manager.setAfk(true);
                    return;
                }
                return;
            } else if (manager.getTimeAfk() >= Settings.Afk.KICK_TIME_IN_SECONDS)
                pl.kickPlayer(color("&cYou have been kicked for idling more than " + timeToMessage(Settings.Afk.KICK_TIME_IN_SECONDS)));
        }
    }

    private boolean updateAFKPlayer(final Player p) {
        final AFKManager manager = AFKManager.getAfkManager(p);
        final AFKManager.MouseLocation OldMouseLocation = manager.getMouseLocation();
        final AFKManager.MouseLocation NewMouseLocation = new AFKManager.MouseLocation(p);
        manager.setMouseLocation(NewMouseLocation);
        if (OldMouseLocation != null && manager.isLocEqual(OldMouseLocation)) {
            manager.setTimeAfk(manager.getTimeAfk() + Settings.Afk.SECONDS_DELAY);
            return true;
        }
        manager.setTimeAfk(0);
        if (manager.isAfk()) {
            manager.setAfk(false);
        }
        return false;
    }
}
