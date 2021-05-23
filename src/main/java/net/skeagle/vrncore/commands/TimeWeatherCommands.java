package net.skeagle.vrncore.commands;

import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.WeatherType;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TimeWeatherCommands {

    @CommandHook("day")
    public void onDay(final Player player) {
        player.getLocation().getWorld().setTime(1000);
        say(player, "Time set to day.");
    }

    @CommandHook("night")
    public void onNight(final Player player) {
        player.getLocation().getWorld().setTime(13000);
        say(player, "Time set to night.");
    }

    @CommandHook("sun")
    public void onSun(final Player player) {
        player.getLocation().getWorld().setStorm(false);
        player.getLocation().getWorld().setThundering(false);
        say(player, "Weather set to sun.");
    }

    @CommandHook("rain")
    public void onRain(final Player player) {
        player.getLocation().getWorld().setStorm(true);
        player.getLocation().getWorld().setThundering(false);
        say(player, "Weather set to rain.");
    }

    @CommandHook("thunder")
    public void onThunder(final Player player) {
        player.getLocation().getWorld().setStorm(true);
        player.getLocation().getWorld().setThundering(true);
        say(player, "Weather set to thunder.");
    }

    @CommandHook("pweathersun")
    public void onPweatherSun(final Player player) {
        player.setPlayerWeather(WeatherType.CLEAR);
        say(player, "Personal weather set to sun.");
    }

    @CommandHook("pweatherrain")
    public void onPweatherRain(final Player player) {
        player.setPlayerWeather(WeatherType.DOWNFALL);
        say(player, "Personal weather set to rain.");
    }

    @CommandHook("pweatherreset")
    public void onPweatherReset(final Player player) {
        player.resetPlayerWeather();
        say(player, "Personal weather has been reset.");
    }

    @CommandHook("ptimeday")
    public void onPtimeDay(final Player player) {
        player.setPlayerTime(6000, false);
        say(player, "Personal time set to day.");
    }

    @CommandHook("ptimenight")
    public void onPtimeNight(final Player player) {
        player.setPlayerTime(18000, false);
        say(player, "Personal time set to night.");
    }

    @CommandHook("ptimereset")
    public void onPtimeReset(final Player player) {
        player.resetPlayerTime();
        say(player, "Personal time has been reset.");
    }
}
