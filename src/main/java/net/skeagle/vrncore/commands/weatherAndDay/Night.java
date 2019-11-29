package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Night extends SimpleCommand {
    public Night() {
        super("night");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (p.hasPermission("vrn.time")) {
            if (args.length == 0) {
                p.getLocation().getWorld().setTime(13000);
                p.sendMessage(VRNcore.vrn + "Time set to night.");
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("all")) {
                    for(World w : Bukkit.getServer().getWorlds()) {
                        w.setTime(13000);
                    }
                    p.sendMessage(VRNcore.vrn + "Time set to night in all worlds.");
                }
                else {
                    p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/night all.'");
                }
            }
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}
