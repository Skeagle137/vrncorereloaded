package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.NickNameUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class RemoveNick extends SimpleCommand {
    private NickNameUtil util;

    public RemoveNick(NickNameUtil util) {
        super("removenick");
        this.util = util;
        setDescription("Remove a nickname from yourself or another player.");
    }

    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            Player p = getPlayer();
            hasPerm("vrn.nick.self");
            util.setNickName(p.getUniqueId(), null);
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());
            say(p, "&aNickname successfully removed.");
            return;
        }
        hasPerm("vrn.nick.other");
        Player a = findPlayer(args[0], VRNcore.noton);
        util.setNickName(a.getUniqueId(), null);
        a.setDisplayName(a.getName());
        a.setPlayerListName(a.getName());
        say(getSender(), "&7Removed nickname for &a" + a.getName() + ".");
        say(a, "&7Your nickname was disabled.");
    }
}
