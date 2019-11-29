package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;

public class Kill extends SimpleCommand {

    public Kill() {
        super("kill", Collections.singletonList("slap"));
    }

    @Override
    protected void onCommand() {
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("vrn.kill.self")) {
                p.setHealth(0);
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        } else if (args.length == 1) {
            if (p.hasPermission("vrn.kill.others")) {
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
    }
}
