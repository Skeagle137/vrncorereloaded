package net.skeagle.vrncore.event;

import net.skeagle.vrncore.utils.AFKManager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class AFKListener implements Listener {

    @EventHandler
    public void onLeave(final PlayerQuitEvent e) {
        final AFKManager manager = AFKManager.getAfkManager(e.getPlayer());
        if (manager.isAfk()) manager.setAfk(false);
    }
}
