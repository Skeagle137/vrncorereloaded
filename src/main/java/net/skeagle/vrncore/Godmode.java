package net.skeagle.vrncore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.ArrayList;

public class Godmode implements CommandExecutor, Listener {
    private static ArrayList<String> god = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("vrn.god.self")) {
                if (!god.contains(p.getName())) {
                    god.add(p.getName());
                    p.sendMessage(VRNcore.vrn + "You are now invulnerable.");
                } else {
                    god.remove(p.getName());
                    p.sendMessage(VRNcore.vrn + "You are no longer invulnerable.");
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        } else if (args.length == 1) {
            if (p.hasPermission("vrn.god.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    if (!god.contains(a.getName())) {
                        god.add(a.getName());
                        a.sendMessage(VRNcore.vrn + "You are now invulnerable.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + " &7is now invulnerable."));
                    } else {
                        god.remove(a.getName());
                        a.sendMessage(VRNcore.vrn + "You are no longer invulnerable.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + " &7is no longer invulnerable."));
                    }
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }

    @EventHandler
    public void onGodDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (god.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }
}