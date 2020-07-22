package net.skeagle.vrncore.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrncore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.InputStreamReader;
import java.net.URL;

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

    public static String color(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.length() - i > 8) {
                final String temp = s.substring(i, i + 8);
                if (temp.startsWith("&#")) {
                    final char[] chars = temp.replaceFirst("&#", "").toCharArray();
                    final StringBuilder rgbColor = new StringBuilder();
                    rgbColor.append("&x");
                    for (final char c : chars) {
                        rgbColor.append("&").append(c);
                    }
                    s = s.replaceAll(temp, rgbColor.toString());
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] color(final String... i) {
        for (final String uncolored : i) {
            color(uncolored);
        }
        return i;
    }

    public static void sayActionBar(final Player p, final String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color(msg)));
    }

    public static String[] getSkin(final String name) {
        String texture = null;
        String signature = null;
        try {
            final URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            final InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            final String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            final URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            final JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            texture = textureProperty.get("value").getAsString();
            signature = textureProperty.get("signature").getAsString();
        } catch (final Exception ignored) {
        }
        if (texture == null || signature == null) {
            return null;
        }
        return new String[]{texture, signature};
    }

    public static class LocationSerialization {
        public static String serialize(final Location loc) {
            if (loc == null || loc.getWorld() == null) {
                return null;
            }
            return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
        }

        public static Location deserialize(final String s) {
            final String[] split = s.split(" ");
            if (split.length != 6) {
                return null;
            }
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    }
}
