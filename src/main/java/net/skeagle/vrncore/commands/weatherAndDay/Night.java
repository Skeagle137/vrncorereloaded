package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Night extends SimpleCommand {
    public Night() {
        super("night");
        setDescription("Set the time to night.");
        setPermission("vrn.time");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setTime(13000);
        say(getPlayer(),"Time set to night.");
    }
}
