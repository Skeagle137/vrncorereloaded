package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.PlayerSitUtil;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Sit extends SimpleCommand {

    public Sit() {
        super("sit");
        setDescription("Defy all known laws of minecraft physics and sit anywhere.");
        setPermission("vrn.sit");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        PlayerSitUtil p = PlayerSitUtil.getPlayer(getPlayer());
        if (p.isSitting())
            p.setSitting(false);
        else if (Math.abs(getPlayer().getVelocity().getY()) < 0.5 && VRNUtil.getBlockExact(getPlayer().getLocation()) != null)
            p.setSitting(true);
        else
            say(getPlayer(), "&cYou cannot sit while in the air.");
    }
}
