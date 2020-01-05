package net.skeagle.vrncore.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class QuitEvent implements Listener {
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
		event.setQuitMessage(null);
        Bukkit.broadcastMessage(color("&7[&c-&7] &5" + event.getPlayer().getName() + " &dhas left."));
    }

}
