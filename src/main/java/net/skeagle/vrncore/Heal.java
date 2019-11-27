package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Heal implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		Player p = (Player) sender;
		if (args.length == 0) {
            if (p.hasPermission("vrn.heal.self")) {
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setFireTicks(0);
                p.sendMessage(VRNcore.vrn + "Your health and hunger are now full.");
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        else if (args.length == 1) {
            if (p.hasPermission("vrn.heal.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.setHealth(20);
                    a.setFoodLevel(20);
                    a.setFireTicks(0);
                    a.sendMessage(VRNcore.vrn + "Your health and hunger are now full.");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + "&7's health and hunger are now full."));
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

}
