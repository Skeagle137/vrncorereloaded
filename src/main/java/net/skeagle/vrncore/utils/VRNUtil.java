package net.skeagle.vrncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class VRNUtil {

    public static void say(CommandSender cs, String message) {
        if (cs == null) return;
        cs.sendMessage(color("&8[&9&lVRN&r&8] &7" + message));
    }

    public static void say(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message) {
            cs.sendMessage(color("&8[&9&lVRN&r&8] &7" + msg));
        }
    }

    public static void log(String message) {
        Bukkit.getConsoleSender().sendMessage(color(message));
    }

    public static void log(String... message) {
        for (String msg : message) {
            Bukkit.getConsoleSender().sendMessage(color(msg));
        }
    }

    public static void logAdmin(Player p, String message) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.hasPermission("small.admin")) {
                say(pl, "&7[&e" + p.getName() + "&7]: &o" + message);
            }
        }
        log("&7[&e" + p.getName() + "&7]: &o" + message);
    }

    public static String color(String i) {
        return org.bukkit.ChatColor.translateAlternateColorCodes('&', i);
    }
}
