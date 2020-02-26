package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.settings.Settings;
import org.bukkit.command.CommandSender;
import org.mineacademy.fo.Common;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class VRNUtil {

    public static void say(final CommandSender cs, final String... message) {
        if (cs == null) return;
        for (final String msg : message) {
            cs.sendMessage(color(Settings.PREFIX + msg));
        }
    }

    public static void sayNoPrefix(final CommandSender cs, final String... message) {
        if (cs == null) return;
        for (final String msg : message) {
            cs.sendMessage(color(msg));
        }
    }

    public static void sayAndLog(final CommandSender cs, final String... message) {
        sayNoPrefix(cs, message);
        Common.logNoPrefix(color(message));
    }

    public static String color(final String i) {
        return translateAlternateColorCodes('&', i);
    }

    public static String[] color(final String... i) {
        for (final String uncolored : i) {
            translateAlternateColorCodes('&', uncolored);
        }
        return i;
    }
}
