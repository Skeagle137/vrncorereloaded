package net.skeagle.vrncore.utils.storage.holograms;

import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class Hologram {
    @Getter
    private final UUID id;
    @Getter
    private final Location loc;
    @Getter
    private String hologram;

    public Hologram(final Location loc, final String hologram, final UUID id) {
        this.id = id;
        this.loc = loc;
        this.hologram = hologram;
    }

    public void setHologram(final String s) {
        hologram = s;
        final boolean loaded = loc.getChunk().isLoaded();
        loc.getChunk().load();
        for (final Entity entity : loc.getChunk().getEntities()) {
            if (entity.getUniqueId().toString().equalsIgnoreCase(id.toString())) {
                entity.setCustomName(VRNUtil.color(s));
            }
        }
        if (!loaded) {
            loc.getChunk().unload();
        }
    }

    public void remove() {
        final boolean loaded = loc.getChunk().isLoaded();
        loc.getChunk().load();
        for (final Entity entity : loc.getChunk().getEntities()) {
            if (entity.getUniqueId().toString().equalsIgnoreCase(id.toString())) {
                entity.remove();
            }
        }
        if (!loaded) {
            loc.getChunk().unload();
        }
    }

    public String serialize(final Hologram holo) {
        return serializeHoloLoc(getLoc()) + " " + getHologram() + " " + getId().toString();
    }

    public static Hologram deserialize(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split(" ");
        if (split.length != 3) {
            return null;
        }
        return new Hologram(deserializeHoloLoc(split[0]), split[1], UUID.fromString(split[0]));
    }

    private String serializeHoloLoc(final Location location) {
        if (location == null) {
            return null;
        }
        return location.getWorld().getName() + ";" + location.getX() + ";" + location.getY() + ";" + location.getZ();
    }

    private static Location deserializeHoloLoc(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split(";");
        if (split.length != 4) {
            return null;
        }
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }
}
