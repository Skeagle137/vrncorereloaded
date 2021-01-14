package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class RemoveNick extends SimpleCommand {

    public RemoveNick() {
        super("removenick");
        setDescription("Remove a nickname from yourself or another player.");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final Player p = getPlayer();
            hasPerm("vrn.nick.self");
            final PlayerData data = PlayerManager.getData(p);
            data.setNickname(null);
            p.setDisplayName(p.getName());
            p.setPlayerListName(p.getName());
            say(p, "&aNickname successfully removed.");
            return;
        }
        hasPerm("vrn.nick.other");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final PlayerData data = PlayerManager.getData(a);
        data.setNickname(null);
        a.setDisplayName(a.getName());
        a.setPlayerListName(a.getName());
        say(getSender(), "&7Removed nickname for &a" + a.getName() + ".");
        say(a, "&7Your nickname was disabled.");
    }
}
