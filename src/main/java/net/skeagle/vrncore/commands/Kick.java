package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class Kick extends SimpleCommand {
    public Kick() {
        super("kick");
        setMinArguments(1);
        setUsage("<player> [reason]");
        setDescription("Kick a player from the server.");
        setPermission("vrn.kick");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        final StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        final String reason = sb.toString();

        final Player a = findPlayer(args[0], VRNUtil.noton);
        if (args.length == 1) {
            a.kickPlayer(color("&cYou have been kicked by " + getSender().getName() + "."));
            Bukkit.broadcastMessage(color("&a" + a.getName() + " &7was kicked by&c " + getSender().getName() + "&7."));
            return;
        }
        a.kickPlayer(color("&cYou have been kicked for: \n\n &6" + reason + "\n\n " + "&cby: &b" + getSender().getName()));
        Bukkit.broadcastMessage(color("&a" + a.getName() + " &7was kicked by&c " + getSender().getName() + "&7 for: &b" + reason));
    }
}
