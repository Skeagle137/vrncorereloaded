package net.skeagle.vrncore.utils;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import net.skeagle.vrnlib.VRNLib;
import net.skeagle.vrnlib.commandmanager.Messages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.logging.Level;

public final class VRNUtil {

    public static String noperm = color(Messages.getLoaded(VRNLib.getInstance()).get("noPermission"));

    public static void say(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(Messages.msg("prefix") + " " + msg));
    }

    public static void say(VRNPlayer p, String... message) {
        for (String msg : message)
            p.getPlayer().sendMessage(color(Messages.msg("prefix") + " " + msg));
    }

    public static void sayNoPrefix(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(msg));
    }

    public static void sayNoPrefix(VRNPlayer p, String... message) {
        for (String msg : message)
            p.getPlayer().sendMessage(color(msg));
    }

    public static boolean hasPerm(VRNPlayer p, String perm) {
        return p.getPlayer().hasPermission(perm);
    }

    public static void log(String... messages) {
        log(Level.INFO, messages);
    }

    public static void log(Level level, String... messages) {
        if (messages == null) return;
        for (String s : messages)
            Bukkit.getLogger().log(level, color(s));
    }

    public static String color(String s) {
        for (int i = 0; i < s.length(); ++i) {
            if (s.length() - i > 8) {
                String temp = s.substring(i, i + 8);
                if (temp.startsWith("&#")) {
                    char[] chars = temp.replaceFirst("&#", "").toCharArray();
                    StringBuilder rgbColor = new StringBuilder();
                    rgbColor.append("&x");
                    for (char c : chars)
                        rgbColor.append("&").append(c);
                    s = s.replaceAll(temp, rgbColor.toString());
                }
            }
        }
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public static String[] color(String... i) {
        for (String uncolored : i)
            color(uncolored);
        return i;
    }

    public static void sayActionBar(Player p, String msg) {
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color(msg)));
    }

    public static Block getStandingBlock(Location loc) {
        Location l = loc.clone();
        Block standing = l.add(0, -1, 0).getBlock();
        double x = l.getX() - (double) l.getBlockX();
        double z = l.getZ() - (double) l.getBlockZ();
        Block b1 = standing.getLocation().clone().add(1, 0, 0).getBlock();
        Block b2 = standing.getLocation().clone().add(1, 0, 1).getBlock();
        Block b3 = standing.getLocation().clone().add(0, 0, 1).getBlock();
        Block b4 = standing.getLocation().clone().add(-1, 0, 1).getBlock();
        Block b5 = standing.getLocation().clone().add(-1, 0, 0).getBlock();
        Block b6 = standing.getLocation().clone().add(-1, 0, -1).getBlock();
        Block b7 = standing.getLocation().clone().add(0, 0, -1).getBlock();
        Block b8 = standing.getLocation().clone().add(1, 0, -1).getBlock();
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

    private static boolean blockCheck(Block b) {
        return (!b.getType().isAir() && b.getType().isSolid());
    }

    public static class LocationSerialization {
        public static String serialize(Location loc) {
            if (loc == null || loc.getWorld() == null)
                return null;
            return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
        }

        public static Location deserialize(String s) {
            if (s == null) return null;
            String[] split = s.split(" ");
            if (split.length != 6)
                return null;
            return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                    Float.parseFloat(split[4]), Float.parseFloat(split[5]));
        }
    }
}
