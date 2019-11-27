package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Broadcast implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String message = sb.toString();
        if (p.hasPermission("vrn.broadcast")) {
            if (args.length == 0) {
                p.sendMessage(VRNcore.no + "You must have a message to broadcast.");
            }
            if (args.length >= 1) {
                Bukkit.broadcastMessage(VRNcore.broadcast + message);
            }
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
    }
}
