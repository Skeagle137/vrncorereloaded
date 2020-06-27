package net.skeagle.vrncore.event;

import net.skeagle.vrncore.utils.storage.npc.NPCResource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class UpdateNPCsListener implements Listener {

    @EventHandler
    public void onJoin(final PlayerJoinEvent e) {
        NPCResource.getInstance().updateNPCsForPlayer(e.getPlayer());
    }
}
