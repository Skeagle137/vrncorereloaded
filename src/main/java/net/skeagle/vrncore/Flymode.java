package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Flymode implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("vrn.fly.self")) {
                p.setAllowFlight(!p.getAllowFlight());
                p.sendMessage(VRNcore.vrn + "Fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        } else if (args.length == 1) {
            if (p.hasPermission("vrn.fly.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.setAllowFlight(!a.getAllowFlight());
                    a.sendMessage(VRNcore.vrn + "Fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
                    p.sendMessage(VRNcore.vrn2 + a.getName() + "'s fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
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

