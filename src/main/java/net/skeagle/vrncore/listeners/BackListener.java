package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.utils.BackUtil;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackListener implements Listener {

    private BackUtil back = BackUtil.getBack();

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            back.setBackLoc(e.getPlayer().getUniqueId(), e.getFrom());
        }

    }
}
