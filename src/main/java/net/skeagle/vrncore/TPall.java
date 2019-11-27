package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TPall implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (p.hasPermission("vrn.tpall")) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (pl != p) {
                    pl.sendMessage(VRNcore.vrn + "Teleporting...");
                    pl.teleport(p.getLocation());
                }
            }
            p.sendMessage(VRNcore.vrn + "Teleported all players.");
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
    }
}
