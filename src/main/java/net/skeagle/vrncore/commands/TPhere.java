package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class TPhere extends SimpleCommand {

    public TPhere() {
        super("tphere");
    }

    @Override
    public void onCommand() {
        checkConsole();
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
    }
}
