package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.settings.YamlConfig;

public class AFKListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        if (cache.getTimeplayed() == null)
            cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(0));
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        final AFKManager manager = AFKManager.getAfkManager(e.getPlayer());
        if (manager.isAfk()) manager.setAfk(false);
    }


}
