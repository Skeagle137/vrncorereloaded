package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static org.bukkit.WeatherType.CLEAR;
import static org.bukkit.WeatherType.DOWNFALL;

public class Pweather extends SimpleCommand {
    public Pweather() {
        super("pweather");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (p.hasPermission("vrn.pweather")) {
            if (args.length == 0) {
                p.sendMessage(VRNcore.no + "You need to specify 'sun,' or 'rain.'");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("sun")) {
                    p.setPlayerWeather(CLEAR);
                    p.sendMessage(VRNcore.vrn + "Personal weather set to sun.");
                } else if (args[0].equalsIgnoreCase("rain")) {
                    p.setPlayerWeather(DOWNFALL);
                    p.sendMessage(VRNcore.vrn + "Personal weather set to rain.");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    p.resetPlayerWeather();
                    p.sendMessage(VRNcore.vrn + "Personal weather has been reset.");
                } else {
                    p.sendMessage(VRNcore.no + "That is not a valid argument.");
                }
            }
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}