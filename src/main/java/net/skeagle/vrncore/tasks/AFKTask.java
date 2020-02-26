package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.afk.AFKManager;
import net.skeagle.vrncore.utils.afk.AFKPlayer;
import net.skeagle.vrncore.utils.afk.MouseLocation;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.Remain;
import org.mineacademy.fo.settings.YamlConfig;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class AFKTask extends BukkitRunnable {

    private AFKManager manager = new AFKManager();

    @Override
    public void run() {
        for (Player pl : Remain.getOnlinePlayers()) {
            final AFKPlayer afkPlayer = manager.getOnlinePlayers().get(pl.getUniqueId());
            if (afkPlayer != null) {
                PlayerCache cache = PlayerCache.getCache(afkPlayer.getPlayer());
                int time = cache.getTimeplayed().getTimeSeconds();
                if (!updateAFKPlayer(afkPlayer)) {
                    say(pl, "test");
                    return;
                }
                if (Settings.Afk.STOP_COUNTING < 0 || afkPlayer.getSecondsAFK() <= Settings.Afk.STOP_COUNTING) {
                    time += Settings.Afk.SECONDS_DELAY;
                    cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(time));
                    say(pl, String.valueOf(cache.getTimeplayed().getTimeSeconds()));
                }
                if (afkPlayer.getSecondsPlayed() >= Settings.Afk.KICK_TIME_IN_SECONDS)
                    afkPlayer.getPlayer().kickPlayer(color("&cYou have been kicked for idling more than " + afkPlayer.timeToMessage(Settings.Afk.KICK_TIME_IN_SECONDS)));
            }
        }
    }

    private boolean updateAFKPlayer(final AFKPlayer afkPlayer) {
        final MouseLocation lastMouseLocation = new MouseLocation(afkPlayer.getPlayer());
        final MouseLocation lastMouseLocation2 = afkPlayer.getLastMouseLocation();
        afkPlayer.setLastMouseLocation(lastMouseLocation);
        if (lastMouseLocation2 != null && lastMouseLocation.isMouseLocationEqual(lastMouseLocation2)) {
            afkPlayer.setSecondsAFK(afkPlayer.getSecondsAFK() + Settings.Afk.SECONDS_DELAY);
            return true;
        }
        afkPlayer.setSecondsAFK(0);
        return false;
    }

    /*
    private void checkPlayerAgainstTimeNodes(final AFKPlayer afkPlayer) {
        for (final ActionNode actionNode : this.actionPlayTimeNodes) {
            if (afkPlayer.getSecondsPlayed() == actionNode.getSecondsToExecute() || actionNode.isRepeatAble()) {
                if (actionNode.isRepeatAble() && afkPlayer.getSecondsPlayed() % actionNode.getSecondsToExecute() != 0) {
                    continue;
                }
                Bukkit.getScheduler().runTask(VRNcore.getInstance(), () -> actionNode.execute(afkPlayer.getPlayer()));
            }
        }
    }
    */
}
