package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPhere implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (args.length == 0) {
            p.sendMessage(VRNcore.sp);
        } else if (args.length == 1) {
            if (p.hasPermission("vrn.tphere")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.sendMessage(VRNcore.vrn + "Teleporting...");
                    a.teleport(p.getLocation());
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
