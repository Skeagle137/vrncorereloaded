package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static org.bukkit.WeatherType.CLEAR;
import static org.bukkit.WeatherType.DOWNFALL;

public class Pweather extends SimpleCommand {
    public Pweather() {
        super("pweather");
        setMinArguments(1);
        setUsage("<sun|rain|reset>");
        setDescription("Change your personal weather.");
        setPermission("vrn.pweather");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args[0].equalsIgnoreCase("sun")) {
            p.setPlayerWeather(CLEAR);
            say(p,"Personal weather set to sun.");
        } else if (args[0].equalsIgnoreCase("rain")) {
            p.setPlayerWeather(DOWNFALL);
            say(p,"Personal weather set to rain.");
        } else if (args[0].equalsIgnoreCase("reset")) {
            p.resetPlayerWeather();
            say(p,"Personal weather has been reset.");
        } else {
            say(p,"&cThat is not a valid argument.");
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 0) {
            return completeLastWord("sun", "rain", "reset");
        }
        return new ArrayList<>();
    }
}