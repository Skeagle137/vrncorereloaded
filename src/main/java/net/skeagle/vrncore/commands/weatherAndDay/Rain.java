package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Rain extends SimpleCommand {
    public Rain() {
        super("rain");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (p.hasPermission("vrn.weather")) {
            if (args.length == 0) {
                p.getLocation().getWorld().setStorm(true);
                p.sendMessage(VRNcore.vrn + "Weather set to rain.");
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    for(World w : Bukkit.getServer().getWorlds()) {
                        w.setStorm(true);
                    }
                    p.sendMessage(VRNcore.vrn + "Weather set to rain in all worlds.");
                }
                else {
                    p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/rain all.'");
                }
            }
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}