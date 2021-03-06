package net.skeagle.vrncore.utils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitScheduler;
import org.bukkit.scheduler.BukkitTask;

import java.io.InputStreamReader;
import java.net.URL;

public final class VRNUtil {

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

    public static Block getBlockExact(final Location loc) {
        final Location l = loc.clone();
        final Block standing = l.add(0, -1, 0).getBlock();
        final double x = l.getX() - (double) l.getBlockX();
        final double z = l.getZ() - (double) l.getBlockZ();
        final Block b1 = standing.getLocation().clone().add(1, 0, 0).getBlock();
        final Block b2 = standing.getLocation().clone().add(1, 0, 1).getBlock();
        final Block b3 = standing.getLocation().clone().add(0, 0, 1).getBlock();
        final Block b4 = standing.getLocation().clone().add(-1, 0, 1).getBlock();
        final Block b5 = standing.getLocation().clone().add(-1, 0, 0).getBlock();
        final Block b6 = standing.getLocation().clone().add(-1, 0, -1).getBlock();
        final Block b7 = standing.getLocation().clone().add(0, 0, -1).getBlock();
        final Block b8 = standing.getLocation().clone().add(1, 0, -1).getBlock();
        //check direct block
        if (blockCheck(standing)) {
            return standing;
        }
        //check adjacent
        if (x > 0.7 && blockCheck(b1)) {
            return b1;
        }
        if (x < 0.3 && blockCheck(b5)) {
            return b5;
        }
        if (z > 0.7 && blockCheck(b3)) {
            return b3;
        }
        if (z < 0.3 && blockCheck(b7)) {
            return b7;
        }
        //corners
        if (x > 0.7 && z > 0.7 && blockCheck(b2)) {
            return b2;
        }
        if (x < 0.3 && z > 0.7 && blockCheck(b4)) {
            return b4;
        }
        if (x > 0.7 && z < 0.3 && blockCheck(b8)) {
            return b8;
        }
        if (x < 0.3 && z < 0.3 && blockCheck(b6)) {
            return b6;
        }
        //no ground in 3x3 area
        return null;
    }

    private static boolean blockCheck(final Block b) {
        return (!b.getType().isAir() && b.getType().isSolid());
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
            if (s == null) return null;
            final String[] split = s.split(" ");
            if (split.length != 6) {
                return null;
            }
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    }

    public static class Tasks {
        int id;

        private BukkitTask setupId(int id) {
            this.id = id;

            BukkitTask task;
            do {
                if (!Bukkit.getScheduler().getPendingTasks().iterator().hasNext())
                    return null;
                task = Bukkit.getScheduler().getPendingTasks().iterator().next();
            } while (task.getTaskId() != id);

            return task;
        }

        public static Runnable run(Runnable r) {
            return run(0, r);
        }

        public static Runnable run(int delay, Runnable r) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            return (Runnable) (delay != 0 ? scheduler.runTask(VRNcore.getInstance(), r) : scheduler.runTaskLater(VRNcore.getInstance(), r, 0));
        }

        public static Runnable runAsync(Runnable r) {
            return runAsync(0, r);
        }

        public static Runnable runAsync(int delay, Runnable r) {
            BukkitScheduler scheduler = Bukkit.getScheduler();
            return (Runnable) (delay != 0 ? scheduler.runTaskAsynchronously(VRNcore.getInstance(), r) : scheduler.runTaskLaterAsynchronously(VRNcore.getInstance(), r, 0));
        }
    }
}
