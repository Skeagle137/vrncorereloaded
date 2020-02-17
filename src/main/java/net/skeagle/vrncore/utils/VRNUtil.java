package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.settings.Settings;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.Common;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class VRNUtil {

    public static void say(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message) {
            cs.sendMessage(color(Settings.PREFIX + msg));
        }
    }

    public static void sayNoPrefix(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message) {
            cs.sendMessage(color(msg));
        }
    }

    public static void sayAndLog(CommandSender cs, String... message) {
        sayNoPrefix(cs, message);
        Common.logNoPrefix(color(message));
    }

    public static String color(String i) {
        return translateAlternateColorCodes('&', i);
    }

    public static String[] color(String... i) {
        for (String uncolored : i) {
            translateAlternateColorCodes('&', uncolored);
        }
        return i;
    }
}
