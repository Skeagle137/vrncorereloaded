package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class TPall extends SimpleCommand {

    public TPall() {
        super("tpall");
    }

    @Override
    public void onCommand() {
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
    }
}
