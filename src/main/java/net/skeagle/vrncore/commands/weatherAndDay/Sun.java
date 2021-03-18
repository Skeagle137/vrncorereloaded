package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Sun extends SimpleCommand {
    public Sun() {
        super("sun");
        setDescription("Set the weather to sunny.");
        setPermission("vrn.weather");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setStorm(false);
        say(getPlayer(), "Weather set to sun.");
    }
}