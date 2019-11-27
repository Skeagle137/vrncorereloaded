package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class Invsee implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("invsee")) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.hasPermission("vrn.invsee.self")) {
                    p.openInventory(p.getInventory());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing your inventory."));
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            } else if (args.length == 1) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (p.hasPermission("vrn.invsee.others")) {
                    if (a != null) {
                        p.openInventory(a.getInventory());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing &a" + a.getName() + "&7's inventory."));
                    } else {
                        p.sendMessage(VRNcore.noton);
                    }
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            } else {
                p.sendMessage(VRNcore.tma);
            }
        }
        if (label.equalsIgnoreCase("echest")) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.hasPermission("vrn.echest.self")) {
                    p.openInventory(p.getEnderChest());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing your inventory."));
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            } else if (args.length == 1) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (p.hasPermission("vrn.echest.others")) {
                    if (a != null) {
                        p.openInventory(a.getEnderChest());
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing &a" + a.getName() + "&7's ender chest."));
                    } else {
                        p.sendMessage(VRNcore.noton);
                    }
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            } else {
                p.sendMessage(VRNcore.tma);
            }
        }
        return true;
    }
}
