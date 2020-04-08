package net.skeagle.vrncore.utils.storage;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationSerialization {

    public static String serialize(final Location loc) {
        if (loc == null || loc.getWorld() == null) {
            return null;
        }
        return loc.getWorld().getName() + " " + loc.getX() + " " + loc.getY() + " " + loc.getZ() + " " + loc.getYaw() + " " + loc.getPitch();
    }

    public static Location deserialize(final String s) {
        final String[] split = s.replaceAll("\"", "").split(" ");
        if (split.length != 6) {
            return null;
        }
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]),
                Float.parseFloat(split[4]), Float.parseFloat(split[5]));
    }
}
