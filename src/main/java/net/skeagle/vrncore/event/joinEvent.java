package net.skeagle.vrncore.event;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.mineacademy.fo.Common;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class joinEvent implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.setJoinMessage(null);
        Player p = event.getPlayer();
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!pl.getName().equals(p.getName())) {
                Bukkit.broadcastMessage(color("&7[&b+&7] &5" + p.getName() + " &dhas joined."));
                if (!p.hasPlayedBefore()) {
                    say(pl,"&e" + p.getName() + " &6has joined for the first time. Welcome, &e" + p.getName() + "&6!");
                    Common.log("&e" + p.getName() + " &6has joined for the first time. Welcome, &e" + p.getName() + "&6!");
                }
            } else {
                if (!p.hasPlayedBefore()) {
                    say(p, "&dWelcome to &b&lVRN Network&r&d, &5" + p.getName() + "&d!");
                    return;
                }
                say(p, "&dWelcome back, &5" + p.getName() + "&d!");
            }
        }
    }
}
