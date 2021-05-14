package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Nick extends SimpleCommand {
    public Nick() {
        super("nick|nickname");
        setUsage("<nickname> or <playername> <nickname>");
        setDescription("Sets nickname for yourself or another player.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            say(getSender(), "You need to provide a nickname.");
            return;
        }
        if (args.length < 2)
            checkConsole();
        final VRNPlayer p = new VRNPlayer(args.length < 2 ? getPlayer() : findPlayer(args[0], VRNUtil.noton));
        checkPerm("vrn.nick." + (args.length < 2 ? "self" : "others"));
        final int index = args.length < 2 ? 0 : 1;
        checkNick(p.getPlayer(), args[index]);
        say(getSender(), "&7Successfully set " +
                (p.getPlayer() == getSender() ? "your" : "&a" + p.getName() + "&7's") + " nick to " + args[index] + "&r&7.");
        p.setName(args[index]);
        if (args.length < 2 || p.getPlayer() == getSender()) return;
        say(p, "&7Your nickname was changed to " + p.getName());
    }

    private void checkNick(final Player p, final String s) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl == p)
                continue;
            if (ChatColor.stripColor(s).equals(ChatColor.stripColor(pl.getDisplayName())))
                returnTell("&cAnother player already has this nickname.");
            if (s.equalsIgnoreCase(p.getName()))
                returnTell("&cThe nickname can't be your own name.");
        }
    }
}
