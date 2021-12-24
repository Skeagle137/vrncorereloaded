package net.skeagle.vrncore.utils;

import com.google.gson.Gson;
import net.skeagle.vrncommands.BukkitMessages;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.command.CommandSender;

import java.util.logging.Level;

import static net.skeagle.vrncommands.BukkitUtils.color;

public final class VRNUtil {

    public static final Gson GSON = new Gson();

    public static void say(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(BukkitMessages.msg("prefix") + " " + msg));
    }

    public static void sayNoPrefix(CommandSender cs, String... message) {
        if (cs == null) return;
        for (String msg : message)
            cs.sendMessage(color(msg));
    }

    public static void log(String... messages) {
        log(Level.INFO, messages);
    }

    public static void log(Level level, String... messages) {
        if (messages == null) return;
        for (String s : messages)
            Bukkit.getLogger().log(level, color(s));
    }

    public static void broadcast(boolean silent, String s) {
        Bukkit.broadcast(s, silent ? "vrn.silentbypass" : "");
    }

    public static Block getStandingBlock(Location loc) {
        Block standing = loc.clone().getBlock().getRelative(BlockFace.DOWN);
        double x = loc.getX() % 1;
        double z = loc.getZ() % 1;
        if (blockCheck(standing))
            return standing;
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (i == 0 && j == 0) continue;
                Block b = standing.getLocation().clone().add(i, 0, j).getBlock();
                if (blockCheck(b)) {
                    if (x > 0.7 && z > 0.7) {
                        return b;
                    }
                    if (x < 0.3 && z > 0.7) {
                        return b;
                    }
                    if (x > 0.7 && z < 0.3) {
                        return b;
                    }
                    if (x < 0.3 && z < 0.3) {
                        return b;
                    }
                    if (x > 0.7) {
                        return b;
                    }
                    if (x < 0.3) {
                        return b;
                    }
                    if (z > 0.7) {
                        return b;
                    }
                    if (z < 0.3) {
                        return b;
                    }
                }
            }
        }
        return null;
    }

    private static boolean blockCheck(Block b) {
        return (!b.getType().isAir() && b.getType().isSolid());
    }

    public static <T extends Throwable, U> U sneakyThrow(Throwable t) throws T {
        throw (T) t;
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
