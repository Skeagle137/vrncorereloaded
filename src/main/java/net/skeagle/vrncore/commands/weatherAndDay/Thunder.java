package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Thunder extends SimpleCommand {
    public Thunder() {
        super("thunder");
        setDescription("Set the weather to rain and thunder.");
        setPermission("vrn.weather");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setStorm(true);
        getPlayer().getLocation().getWorld().setThundering(true);
        say(getPlayer(), "Weather set to thunder.");
    }
}
