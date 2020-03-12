package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Flymode extends SimpleCommand {

    public Flymode() {
        super("fly");
        setDescription("Toggle fly mode for yourself or another player.");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            hasPerm("vrn.fly.self");
            p.setAllowFlight(!p.getAllowFlight());
            say(p, "Fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
            return;
        }
        hasPerm("vrn.fly.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        a.setAllowFlight(!a.getAllowFlight());
        say(a, "Fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
        say(p, "&a" + a.getName() + "&7's fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
    }
}

