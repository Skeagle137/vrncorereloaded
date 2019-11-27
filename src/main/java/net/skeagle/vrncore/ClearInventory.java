package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClearInventory implements CommandExecutor {
	
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		//disabled for interference with normal /clear
	    Player p = (Player) sender;
		if (args.length == 0) {
            if (p.hasPermission("vrn.clear.self")) {
                p.getInventory().clear();
                p.getInventory().setHelmet(null);
                p.getInventory().setChestplate(null);
                p.getInventory().setLeggings(null);
                p.getInventory().setBoots(null);

                p.sendMessage(VRNcore.vrn + "Your inventory has been cleared.");
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        else if (args.length == 1) {
            if (p.hasPermission("vrn.clear.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.getInventory().clear();
                    a.getInventory().setHelmet(null);
                    a.getInventory().setChestplate(null);
                    a.getInventory().setLeggings(null);
                    a.getInventory().setBoots(null);

                    a.sendMessage(VRNcore.vrn + "Your inventory has been cleared.");
                    p.sendMessage(VRNcore.vrn2 + a.getName() + "&7's inventory has been cleared.");
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
