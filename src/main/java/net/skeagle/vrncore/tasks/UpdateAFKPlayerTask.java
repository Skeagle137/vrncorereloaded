package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.AFKManager;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import net.skeagle.vrncore.utils.storage.timerewards.RewardManager;
import net.skeagle.vrncore.utils.storage.timerewards.TimeRewards;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static net.skeagle.vrncore.utils.TimeUtil.timeToMessage;
import static net.skeagle.vrncore.utils.VRNUtil.color;

public class UpdateAFKPlayerTask extends BukkitRunnable {

    public UpdateAFKPlayerTask(VRNcore vrn) {
        runTaskTimer(vrn, 0, 20);
    }

    @Override
    public void run() {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            final AFKManager manager = AFKManager.getAfkManager(pl);
            final PlayerData data = PlayerManager.getData(pl);
            final TimeRewards reward;
            Long time = data.getTimeplayed();
            if (!updateAFKPlayer(pl) || !manager.isAfk()) {
                reward = RewardManager.getInstance().getReward(String.valueOf(time));
                if (reward != null)
                    if (reward.checkPerm(pl))
                        reward.doReward(pl);
                time += 1;
                data.setTimeplayed(time);
                if (manager.getTimeAfk() > Settings.Afk.STOP_COUNTING)
                    manager.setAfk(true);
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
            manager.setTimeAfk(manager.getTimeAfk() + 1);
            return true;
        }
        manager.setTimeAfk(0);
        if (manager.isAfk())
            manager.setAfk(false);
        return false;
    }
}
