package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.afk.AFKManager;
import net.skeagle.vrncore.utils.afk.UpdatePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.UUID;

public class AFKListener implements Listener {

    private final AFKManager manager = new AFKManager();

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        if (cache.getTimeplayed() == null)
            cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(0));
        AddAFKPlayer(e.getPlayer());
    }

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        removeAFKPlayer(e.getPlayer());
    }

    public void AddAFKPlayer(final Player player) {
        final UUID uniqueId = player.getUniqueId();
        if (!player.isOnline()) {
            return;
        }
        manager.addAFKPlayer(new UpdatePlayer(player, uniqueId));
    }

    public void removeAFKPlayer(final Player player) {
        if (player == null) {
            return;
        }
        final UpdatePlayer afkPlayer = manager.getOnlinePlayers().get(player.getUniqueId());
        if (afkPlayer == null) {
            return;
        }
        manager.removeAFKPlayer(afkPlayer);
    }


}
