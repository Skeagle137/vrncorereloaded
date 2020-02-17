package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Realname extends SimpleCommand {

    public Realname() {
        super("realname");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Look up a player's real name from their nick name.");
        setPermission("vrn.realname");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            PlayerCache cache = PlayerCache.getCache(pl);
            if (cache.getNickname().equalsIgnoreCase(args[0])) {
                say(getSender(), "&7The player with the nickname " + args[0] + "&r&7 is called &a" + pl.getName() + "&7.");
                return;
            }
        }
        say(getSender(), "&cThere is no player online that has that nickname.");
    }
}
