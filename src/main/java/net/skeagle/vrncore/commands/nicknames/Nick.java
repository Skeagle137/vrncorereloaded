package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.NickNameUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Nick extends SimpleCommand {
    private NickNameUtil util;

    public Nick(NickNameUtil util) {
        super("nick|nickname");
        this.util = util;

        setMinArguments(1);
        setUsage("<nickname> or <playername> <nickname>");
        setDescription("Sets nickname for yourself or another player.");
    }

    public void onCommand() {
        if (args.length == 1) {
            checkConsole();
            Player p = getPlayer();
            hasPerm("vrn.nick.self");
            String nick = color(args[0] + "&r");
            util.setNickName(p.getUniqueId(), nick);
            p.setDisplayName(p.getName());
            p.setDisplayName(nick);
            p.setPlayerListName(nick);
            say(p, "&aNickname successfully changed.");
            return;
        }
        hasPerm("nicknames.nick.other");
        Player a = findPlayer(args[0], VRNcore.noton);
        String nick = color(args[1] + "&r");
        util.setNickName(a.getUniqueId(), nick);
        a.setDisplayName(nick);
        a.setPlayerListName(nick);
        say(getSender(), "&7Successfully set &a" + a.getName() + "&7's nick to " + nick + "&r&7.");
    }
}
