package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Rain extends SimpleCommand {
    public Rain() {
        super("rain");
        setDescription("Set the weather to rainy.");
        setPermission("vrn.weather");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setStorm(true);
        say(getPlayer(), "Weather set to rain.");
    }
}