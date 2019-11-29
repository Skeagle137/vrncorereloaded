package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class QuitEvent implements Listener {
	@EventHandler
    public void onPlayerQuit(PlayerQuitEvent event)
    {
		event.setQuitMessage(null);
        Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.joinquit + "&5" + event.getPlayer().getName() + " &dhas left."));
    }

}
