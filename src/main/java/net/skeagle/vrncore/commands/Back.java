package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.BackUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Back extends SimpleCommand {

    public Back() {
        super("back");
        setDescription("Teleport back to a player's previous location.");
        setPermissionMessage(VRNUtil.noperm);
    }

    BackUtil back = BackUtil.getBack();

    @Override
    public void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            hasPerm("vrn.back.self");
            if (back.hasBackLoc(p.getUniqueId())) {
                back.teleToBackLoc(p.getUniqueId(), p);
                say(p, "&7Teleported to your last location.");
                return;
            }
            say(p, "&cYou do not have anywhere to teleport back to.");
            return;
        }
        final Player a = findPlayer(args[0], VRNUtil.noton);
        hasPerm("vrn.back.others");
        if (back.hasBackLoc(a.getUniqueId())) {
            back.teleToBackLoc(p.getUniqueId(), a);
            say(p, "&7Teleported to &a" + a.getName() + "&7's last location.");
            return;
        }
        say(p, "&a" + a.getName() + " &7does not have a saved last location.");
    }
}
