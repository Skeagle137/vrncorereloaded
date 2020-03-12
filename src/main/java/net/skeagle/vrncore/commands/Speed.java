package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Speed extends SimpleCommand {

    public Speed() {
        super("speed");
        setUsage("<speed>");
        setDescription("Change your fly and walk speed.");
        setPermission("vrn.speed");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            if (p.isFlying()) {
                p.setFlySpeed((float) 0.1);
            } else {
                p.setWalkSpeed((float) 0.2);
            }
            say(p, "Your " + (p.isFlying() ? "flying" : "walking") + " speed has been reset.");
            return;
        }
        if (isInt(args[0])) {
            final int speed = Integer.parseInt(args[0]);
            if (p.isFlying()) {
                p.setFlySpeed((float) calcFly(speed));
            } else {
                p.setWalkSpeed((float) calcWalk(speed));
            }
            say(p, "Your " + (p.isFlying() ? "flying" : "walking") + " speed has been set to &a" + (p.isFlying() ? (calcFly(speed) / 10) : ((int) calcWalk(speed) / 10)) + "&7.");
        } else {
            say(p, "&cThe speed value specified is not a number.");
        }
    }

    private boolean isInt(final String s) {
        try {
            Integer.parseInt(s);
        } catch (final NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    private double calcFly(final int speed) {
        if (speed > 10) {
            return 1;
        }
        if (speed < 0) {
            return 0;
        }
        return (double) speed / 10;
    }

    private double calcWalk(final int speed) {
        if (speed > 10) {
            return 1;
        }
        if (speed < 0) {
            return 0;
        }
        return 0.2 + (0.08 * speed);
    }
}
