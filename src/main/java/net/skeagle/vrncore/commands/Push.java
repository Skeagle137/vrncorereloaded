package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Push extends SimpleCommand {

    public Push() {
        super("push");
        setMinArguments(1);
        setUsage("<player> [multiplier");
        setDescription("Pushes a player and sends them flying backwards.");
        setPermission("vrn.push");
    }

    @Override
    public void onCommand() {
        double multiplier = (args.length < 2 ? 1 : 0.5 * findNumber(1, "&cPush multiplier must be a valid number."));
        if (multiplier > 100) {
            multiplier = 100;
        }
        if (multiplier < 1) {
            multiplier = 0.2;
        }
        final Player p = findPlayer(args[0], VRNUtil.noton);
        p.setVelocity(p.getEyeLocation().getDirection().multiply(-1.5 * multiplier).setY(2 * multiplier));
        say(getSender(), "&eYou pushed " + p.getName() + "!");
    }
}