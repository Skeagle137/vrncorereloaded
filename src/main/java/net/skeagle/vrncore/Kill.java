package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Kill implements CommandExecutor { //or "slap"...
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
            Player p = (Player) sender;
            if (args.length == 0) {
                if (p.hasPermission("vrn.slap.self")) {
                    p.setHealth(0);
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            } else if (args.length == 1) {
                if (p.hasPermission("vrn.slap.others")) {
                    Player a = Bukkit.getPlayerExact(args[0]);
                    if (a != null) {
                        a.setHealth(0);
                        p.sendMessage(VRNcore.vrn + "You rekt &a" + a.getName() + "&7.");

                    } else {
                        p.sendMessage(VRNcore.noton);
                    }
                } else {
                    p.sendMessage(VRNcore.noperm);
                }
            }
        return true;
    }
}
