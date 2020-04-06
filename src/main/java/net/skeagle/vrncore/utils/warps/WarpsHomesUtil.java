package net.skeagle.vrncore.utils.warps;

import net.skeagle.vrncore.utils.Resources;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class WarpsHomesUtil {

    private final Resources r;

    public WarpsHomesUtil(final Resources r) {
        this.r = r;
    }

    private String searchForEqual(final String value) {
        if (r.getWarps().get("warps.") != null) {
            for (final String key : r.getWarps().getConfigurationSection("warps.").getKeys(false)) {
                if (value.equalsIgnoreCase(key)) {
                    return key;
                }
            }
        }
        return null;
    }

    public List<String> returnArray() {
        final List<String> keys = new ArrayList<>();
        for (final String key : r.getWarps().getConfigurationSection("warps.").getKeys(false)) {
            r.getWarps().getString(key);
            keys.add(key);
        }
        return keys;
    }

    public void setValues(final Player p, final String value) {
        final String locPath = "warps." + value;
        if (r.getWarps().get(locPath) == null) {
            if (searchForEqual(value) != null) {
                say(p, "&cThat warp already exists.");
                return;
            }
            r.getWarps().set(locPath + ".world", p.getWorld().getName());
            r.getWarps().set(locPath + ".x", p.getLocation().getX());
            r.getWarps().set(locPath + ".y", p.getLocation().getY());
            r.getWarps().set(locPath + ".z", p.getLocation().getZ());
            r.getWarps().set(locPath + ".yaw", p.getLocation().getYaw());
            r.getWarps().set(locPath + ".pitch", p.getLocation().getPitch());
            r.getWarps().save();
            say(p, "&aThe warp was successfully set. Teleport to it with /warp.");
            return;
        }
        say(p, "&cThat warp already exists.");
    }

    public void delValues(final Player p, final String value) {
        String loc = value;
        if (r.getWarps().get("warps." + value) == null) {
            if (searchForEqual(value) == null) {
                say(p, "&cThat warp does not exist.");
                return;
            }
            loc = searchForEqual(value);
        }
        final String locPath = "warps." + loc;
        r.getWarps().set(locPath + ".world", null);
        r.getWarps().set(locPath + ".x", null);
        r.getWarps().set(locPath + ".y", null);
        r.getWarps().set(locPath + ".z", null);
        r.getWarps().set(locPath + ".yaw", null);
        r.getWarps().set(locPath + ".pitch", null);
        r.getWarps().set(locPath, null);
        r.getWarps().save();
        say(p, "&7The warp &a" + loc + "&7 has been deleted.");
    }

    public void teleportToLoc(final Player p, final String value) {
        String locPath = "warps." + value;
        if (r.getWarps().get(locPath) == null) {
            if (searchForEqual(value) == null) {
                say(p, "&cThat warp does not exist.");
                return;
            }
            locPath = "warps." + searchForEqual(value);
        }
        say(p, "&7Teleporting...");
        final String world = (String) r.getWarps().get(locPath + ".world");
        if (world == null) {
            say(p, "&cThe world that this warp is located in does not exist.");
            return;
        }
        p.teleport(new Location(Bukkit.getWorld(world),
                r.getWarps().getDouble(locPath + ".x"),
                r.getWarps().getDouble(locPath + ".y"),
                r.getWarps().getDouble(locPath + ".z"),
                (float) r.getWarps().getDouble(locPath + ".yaw"),
                (float) r.getWarps().getDouble(locPath + ".pitch")));
    }
}
