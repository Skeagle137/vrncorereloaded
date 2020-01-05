package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Day extends SimpleCommand {
    public Day() {
        super("day");
        setDescription("Set the time to day.");
        setPermission("vrn.time");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setTime(1000);
        say(getPlayer(),"Time set to day.");
    }
}
