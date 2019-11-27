package net.skeagle.vrncore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Setspawn implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (label.equalsIgnoreCase("setspawn")) {
            Player p = (Player) sender;
            if (p.hasPermission("vrn.setspawn")) {
                int LocX = (int) p.getLocation().getX();
                int LocY = (int) p.getLocation().getY() + 1;
                int LocZ = (int) p.getLocation().getZ();

                p.getServer().getWorld("world")
                        .setSpawnLocation(LocX, LocY, LocZ);
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Spawn Set at: &ax: " + LocX + "&7, &ay: " + LocY + "&7, &az: " + LocZ + "&7."));
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("spawn")) {
            Player p = (Player) sender;
            if (p.hasPermission("vrn.spawn")) {
                p.sendMessage(VRNcore.vrn + "Teleporting to spawn...");
                p.teleport(p.getWorld().getSpawnLocation());
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }

        return true;
    }
}
