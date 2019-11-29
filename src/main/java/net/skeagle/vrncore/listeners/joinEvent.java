package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class joinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player p = event.getPlayer();
        if (p.hasPlayedBefore()) {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                    if (!pl.getName().equals(p.getName())) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.joinquit + "&5" + p.getName() + " &dhas joined."));
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.joinquit + "Welcome back, &5" + p.getName() + "&d!"));
                }
            }
        } else {
            for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
                if (!pl.getName().equals(p.getName())) {
                    pl.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.joinquit + "&5" + p.getName() + " &dhas joined."));
                } else {
                    Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.welcome + p.getName() + " &6has joined for the first time. Welcome, &e" + p.getName() + "&6!"));
                }
            }
        }

    }
}
