package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Sun extends SimpleCommand {
    public Sun() {
        super("sun");
        setDescription("Set the weather to sunny.");
        setPermission("vrn.weather");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        getPlayer().getLocation().getWorld().setStorm(false);
        say(getPlayer(), "Weather set to sun.");
    }
}