package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
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
        if (args.length < 1)
            checkConsole();
        final VRNPlayer p = new VRNPlayer(args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton));
        checkPerm("vrn.nick." + (args.length < 1 ? "self" : "others"));
        p.setName(p.getPlayer().getName());
        p.getPlayer().setDisplayName(p.getPlayer().getName());
        p.getPlayer().setPlayerListName(p.getPlayer().getName());
        say(getSender(), p.getPlayer() == getSender() ? "&7Removed your nickname." : "&7Removed nickname for &a" + p.getName() + ".");
        if (args.length < 1 || p.getPlayer() == getSender()) return;
        say(p, "&7Your nickname was disabled.");
    }
}
