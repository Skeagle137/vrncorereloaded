package net.skeagle.vrncore;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Speed implements CommandExecutor {
    public boolean isInt(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        double dfs = 0.1;
        float dflys = (float) dfs;
        double ds = 0.2;
        float f = (float) ds;
        Player p = (Player) sender;
        if (p.hasPermission("vrn.speed")) {
            if (args.length == 0) {
                if (p.getFlySpeed() == dflys && p.getFlySpeed() == f) {
                    p.sendMessage(VRNcore.vrn + "Your current fly and walk speeds are the default values.");
                } else {
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Your current fly speed is &a" + p.getFlySpeed() + "&7, and your current walk speed is &a" + p.getWalkSpeed() + "&7."));
                }
            }

            if (args.length == 1) {
                if (p.isFlying()) {
                    if (isInt(args[0])) {
                        int fs = Integer.parseInt(args[0]);
                        if (fs <= 10 && fs >= 0) {
                            double fsp = (double) fs / 10;
                            float fspeed = (float) fsp;
                            p.setFlySpeed(fspeed);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Fly speed set to &a" + fs + "&7."));
                        } else {
                            p.sendMessage(VRNcore.no + "Speed value must be a number 0-10.");
                        }
                    } else {
                        if (args[0].equalsIgnoreCase("reset")) {
                            p.setFlySpeed(f);
                            p.setWalkSpeed(f);
                            p.sendMessage(VRNcore.vrn + "Fly and walk speed reset.");
                        } else {
                            p.sendMessage(VRNcore.no + "Speed value must be a number 0-10.");
                        }
                    }
                } else {
                    if (isInt(args[0])) {
                        int ws = Integer.parseInt(args[0]);
                        if (ws <= 10 && ws >= 0) {
                            double wsp = (double) ws / 10;
                            float wspeed = (float) wsp;
                            p.setWalkSpeed(wspeed);
                            p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Walk speed set to &a" + ws + "&7."));
                        } else {
                            p.sendMessage(VRNcore.no + "Speed value must be a number 0-10.");
                        }
                    } else {
                        if (args[0].equalsIgnoreCase("reset")) {
                            p.setFlySpeed(dflys);
                            p.setWalkSpeed(f);
                            p.sendMessage(VRNcore.vrn + "Fly and walk speed reset.");
                        } else {
                            p.sendMessage(VRNcore.no + "Speed value must be a number 0-10.");
                        }
                    }
                }
            }
        } else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
    }
}
