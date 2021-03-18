package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;


public class RemoveNick extends SimpleCommand {

    public RemoveNick() {
        super("removenick");
        setDescription("Remove a nickname from yourself or another player.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final VRNPlayer p = new VRNPlayer(getPlayer());
            checkPerm("vrn.nick.self");
            say(p, "&aNickname successfully removed.");
            p.setName(p.getPlayer().getName());
            p.getPlayer().setDisplayName(p.getPlayer().getName());
            p.getPlayer().setPlayerListName(p.getPlayer().getName());
            return;
        }
        checkPerm("vrn.nick.other");
        final VRNPlayer a = new VRNPlayer(findPlayer(args[0], VRNUtil.noton));
        say(getSender(), "&7Removed nickname for &a" + a.getName() + ".");
        say(a, "&7Your nickname was disabled.");
        a.setName(a.getPlayer().getName());
        a.getPlayer().setDisplayName(a.getPlayer().getName());
        a.getPlayer().setPlayerListName(a.getPlayer().getName());
    }
}
