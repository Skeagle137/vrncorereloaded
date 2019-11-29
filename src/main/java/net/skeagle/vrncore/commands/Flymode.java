package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Flymode extends SimpleCommand {

    public Flymode() {
        super("fly");
    }

    @Override
    protected void onCommand() {
        checkConsole();
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
    }
}

