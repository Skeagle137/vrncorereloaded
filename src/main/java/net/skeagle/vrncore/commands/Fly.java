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
        if (args.length < 1)
            checkConsole();
        final Player p = args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton);
        checkPerm("vrn.fly." + (args.length < 1 ? "self" : "others"));
        p.setAllowFlight(!p.getAllowFlight());
        say(p, "Fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
        if (args.length < 1 || p == getSender()) return;
        say(getSender(), "&a" + p.getName() + "&7's fly mode has been " + (p.getAllowFlight() ? "enabled" : "disabled") + ".");
    }
}

