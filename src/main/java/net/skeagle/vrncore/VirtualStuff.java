package net.skeagle.vrncore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VirtualStuff implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("craft")) {
            Player p = (Player)sender;
            if (p.hasPermission("vrn.tpall")) {
                p.openWorkbench(null, true);
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("enchant")) {
            Player p = (Player)sender;
            if (p.hasPermission("vrn.enchant")) {
                p.openEnchanting(null, true);
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }
}
