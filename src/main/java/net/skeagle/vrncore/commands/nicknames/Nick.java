package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.color;
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
        if (args.length < 1)
            say(getSender(), "You need to provide a nickname.");
        else if (args.length == 1) {
            checkConsole();
            checkPerm("vrn.nick.self");
            final VRNPlayer p = new VRNPlayer(getPlayer());
            checkNick(getPlayer(), args[0]);
            say(p, "&aNickname successfully changed.");
            p.setName(args[0]);
        }
        else {
            checkPerm("vrn.nick.other");
            final VRNPlayer a = new VRNPlayer(findPlayer(args[0], VRNUtil.noton));
            checkNick(a.getPlayer(), args[1]);
            say(getSender(), "&7Successfully set &a" + a.getName() + "&7's nick to " + args[1] + "&r&7.");
            a.setName(args[1]);
            say(a, "&7Your nickname was changed to " + a.getName());
        }
    }

    private void checkNick(Player p, String s) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl == p)
                continue;
            if (ChatColor.stripColor(s).equals(ChatColor.stripColor(pl.getDisplayName())))
                returnTell("&cYou cannot have a nickname the same as another player.");
        }
    }
}
