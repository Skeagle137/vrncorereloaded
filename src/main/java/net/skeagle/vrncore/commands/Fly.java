package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Fly extends SimpleCommand {

    public Fly() {
        super("fly");
        setDescription("Toggle fly mode for yourself or another player.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            checkPerm("vrn.fly.self");
            p.setAllowFlight(!p.getAllowFlight());
            say(p, "Fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
            return;
        }
        checkPerm("vrn.fly.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        a.setAllowFlight(!a.getAllowFlight());
        say(a, "Fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
        say(p, "&a" + a.getName() + "&7's fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
    }
}

