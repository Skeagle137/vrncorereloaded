package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Nick extends SimpleCommand {
    public Nick() {
        super("nick|nickname");
        setUsage("<nickname> or <playername> <nickname>");
        setDescription("Sets nickname for yourself or another player.");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1)
            say(getSender(), "You need to provide a nickname.");
        else if (args.length == 1) {
            checkConsole();
            final Player p = getPlayer();
            hasPerm("vrn.nick.self");
            final String nick = color(args[0] + "&r");
            final PlayerData data = PlayerManager.getData(p);
            data.setNickname(nick);
            p.setDisplayName(nick);
            p.setPlayerListName(nick);
            say(p, "&aNickname successfully changed.");
        }
        else {
            hasPerm("vrn.nick.other");
            final Player a = findPlayer(args[0], VRNUtil.noton);
            final String nick = color(args[1] + "&r");
            final PlayerData data = PlayerManager.getData(a);
            data.setNickname(nick);
            a.setDisplayName(nick);
            a.setPlayerListName(nick);
            say(getSender(), "&7Successfully set &a" + a.getName() + "&7's nick to " + nick + "&r&7.");
        }
    }
}
