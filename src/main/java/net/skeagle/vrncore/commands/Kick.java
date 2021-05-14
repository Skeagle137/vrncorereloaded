package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

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
        for (int i = 1; i < args.length; i++)
            sb.append(args[i]).append(" ");
        final String reason = sb.toString();

        final Player a = findPlayer(args[0], VRNUtil.noton);
        a.kickPlayer(color("&cYou have been kicked by &b" + getSender().getName() + "&c" +
                (args.length == 1 ? "." : " for: \n&6" + reason)));
        Bukkit.broadcastMessage(color("&a" + a.getName() + " &7was kicked by&c " + getSender().getName() + "&7" +
                (args.length == 1 ? "." : " for: &b" + reason)));
    }
}
