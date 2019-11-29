package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;

public class Broadcast extends SimpleCommand {

    public Broadcast() {
        super("broadcast", Collections.singletonList("bc"));
    }

    @Override
    protected void onCommand() {
        StringBuilder sb = new StringBuilder();
        for (String arg : args) {
            sb.append(arg).append(" ");
        }
        String message = sb.toString();
        if (sender.hasPermission("vrn.broadcast")) {
            if (args.length < 1) {
                sender.sendMessage(VRNcore.no + "You must have a message to broadcast.");
            }
            else {
                Bukkit.broadcastMessage(VRNcore.broadcast + message);
            }
        }
        else {
            sender.sendMessage(VRNcore.noperm);
        }
    }
}
