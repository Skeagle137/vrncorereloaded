package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.NickNameUtil;
import org.bukkit.Bukkit;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Realname extends SimpleCommand {

    private NickNameUtil util;

    public Realname(NickNameUtil util) {
        super("realname");
        this.util = util;
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Look up a player's real name from their nick name.");
        setPermission("vrn.realname");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        UUID nick = util.getPlayerFromNickName(args[0]);
        checkNotNull(nick, "&cThere is no player that has that nickname.");
        say(getSender(), "&7The player with the nickname " + args[0] + "&r&7 is called &a" + Bukkit.getPlayer(nick).getName() + "&7.");
    }
}
