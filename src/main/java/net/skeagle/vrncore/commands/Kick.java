package net.skeagle.vrncore.commands;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Kick extends SimpleCommand {
    public Kick() {
        super("kick");
    }

    @Override
    protected void onCommand() {
        Player p = (Player) sender;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String reason = sb.toString();

        reason = reason.substring(0, reason.length() - 1);
        if (p.hasPermission("vrn.kick")) {
            if (args.length == 0) {
                p.sendMessage(VRNcore.no + "You must specify a player and reason.");
                p.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent.fromLegacyText(VRNcore.color("&a&lC")));
            }
            Player a = Bukkit.getPlayerExact(args[0]);
            if (a != null) {
                if (args.length == 1) {

                    a.kickPlayer(VRNcore.no + "You have been kicked by " + p.getName() + ".");
                    Bukkit.broadcastMessage(VRNcore.color(VRNcore.no + a.getName() + " &7was kicked by&a " + p.getName() + "&7."));
                }
                if (args.length >= 2) {
                    a.kickPlayer(VRNcore.color("&cYou have been kicked for: \n\n &6" + reason + "\n\n " + "&cby: &b" + p.getName()));
                }
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
