package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Smite implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("vrn.smite.self")) {
                p.getWorld().strikeLightning(p.getLocation());
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        else if (args.length == 1) {
            if (p.hasPermission("vrn.smite.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.getWorld().strikeLightning(a.getLocation());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "smited &a" + a.getName() + "&7."));
                }
                else if (args[0].equalsIgnoreCase("all")) {
                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.getWorld().strikeLightning(pl.getLocation());
                    }
                    p.sendMessage(VRNcore.vrn + "smited all players.");
                }
                else {
                    p.sendMessage(VRNcore.noton);
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }
}
