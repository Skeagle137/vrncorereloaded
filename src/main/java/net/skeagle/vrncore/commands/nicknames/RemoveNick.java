package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class RemoveNick extends SimpleCommand {

    public RemoveNick() {
        super("removenick");
        setDescription("Remove a nickname from yourself or another player.");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            Player p = getPlayer();
            hasPerm("vrn.nick.self");
            PlayerCache cache = PlayerCache.getCache(p);
            cache.setNickname(null);
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());
            say(p, "&aNickname successfully removed.");
            return;
        }
        hasPerm("vrn.nick.other");
        Player a = findPlayer(args[0], VRNcore.noton);
        PlayerCache cache = PlayerCache.getCache(a);
        cache.setNickname(null);
        a.setDisplayName(a.getName());
        a.setPlayerListName(a.getName());
        say(getSender(), "&7Removed nickname for &a" + a.getName() + ".");
        say(a, "&7Your nickname was disabled.");
    }
}
