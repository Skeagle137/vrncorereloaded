package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Flymode extends SimpleCommand {

    public Flymode() {
        super("fly");
        setDescription("Toggle fly mode for yourself or another player.");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args.length < 1) {
            hasPerm("vrn.fly.self");
            p.setAllowFlight(!p.getAllowFlight());
            say(p, "Fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
            return;
        }
        hasPerm("vrn.fly.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        a.setAllowFlight(!a.getAllowFlight());
        say(a, "Fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
        say(p, "&a" + a.getName() + "&7's fly mode has been " + (a.getAllowFlight() ? "enabled" : "disabled") + ".");
    }
}

