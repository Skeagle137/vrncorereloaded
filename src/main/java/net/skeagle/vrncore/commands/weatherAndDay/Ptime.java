package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Ptime extends SimpleCommand {
    public Ptime() {
        super("ptime");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (p.hasPermission("vrn.ptime")) {
            if (args.length == 0) {
                p.sendMessage(VRNcore.no + "You need to specify 'day' or 'night.'");
            } else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("day")) {
                    p.setPlayerTime(1000, true);
                    p.sendMessage(VRNcore.vrn + "Personal time set to day.");
                } else if (args[0].equalsIgnoreCase("night")) {
                    p.setPlayerTime(13000, true);
                    p.sendMessage(VRNcore.vrn + "Personal time set to night.");
                } else if (args[0].equalsIgnoreCase("reset")) {
                    p.resetPlayerTime();
                    p.sendMessage(VRNcore.vrn + "Personal time has been reset.");
                } else {
                    p.sendMessage(VRNcore.no + "That is not a valid argument.");
                }
            }
        } else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}