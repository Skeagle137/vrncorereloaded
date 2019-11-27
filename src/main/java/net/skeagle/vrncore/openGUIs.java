package net.skeagle.vrncore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class openGUIs implements CommandExecutor {

    private VRNcore plugin;

    public openGUIs(VRNcore vrncore) {
        plugin = vrncore;
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        Player p = (Player) sender;
        if (label.equalsIgnoreCase("ggive")) {
            if (p.hasPermission("vrn.ggive")) {
                GUIgive g = new GUIgive();
                g.give(p);
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }
}
