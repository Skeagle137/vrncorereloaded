package net.skeagle.vrncore.utils;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrncore.settings.Settings;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import static org.bukkit.ChatColor.translateAlternateColorCodes;

public class VRNUtil {

    public static String noperm = color(Settings.PREFIX + "&cYou do not have permission to do this.");
    public static String noton = color(Settings.PREFIX + "&cThat player is not online.");

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

    public static String timeToMessage(final int time) {
        final long days = time / 86400L;
        final long hours = (time % 86400L) / 3600L;
        final long minutes = (time % 86400L % 3600L) / 60L;
        final long seconds = time % 86400L % 3600L % 60L;
        return ((days != 0) ? days + " " + timeGrammarCheck("days", time) + ", " : "") +
                ((hours != 0) ? hours + " " + timeGrammarCheck("hours", time) + ", " : "") +
                ((minutes != 0) ? minutes + " " + timeGrammarCheck("minutes", time) + ", " : "") +
                ((seconds != 0) ? seconds + " " + timeGrammarCheck("seconds", time) : "");
    }

    private static String timeGrammarCheck(final String s, final int i) {
        if (i != 1) {
            return s;
        }
        return s.substring(s.length() - 1);
    }

    public static void sayActionBar(final Player p, final String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color(msg)));
    }
}
