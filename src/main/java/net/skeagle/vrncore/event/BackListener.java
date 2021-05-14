package net.skeagle.vrncore.event;

import net.skeagle.vrncore.commands.Back;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class BackListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onTeleport(final PlayerTeleportEvent e) {
        if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
            final Back.BackCache back = new Back.BackCache();
            back.setBackLoc(e.getPlayer().getUniqueId(), e.getFrom());
        }
    }
}
