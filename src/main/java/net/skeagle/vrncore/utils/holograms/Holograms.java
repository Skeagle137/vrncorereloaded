package net.skeagle.vrncore.utils.holograms;

import com.google.gson.JsonObject;
import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

import java.util.UUID;

public class Holograms {
    @Getter
    private final UUID id;
    @Getter
    private final Location loc;
    @Getter
    private String hologram;

    public Holograms(final Location loc, final String hologram, final UUID id) {
        this.id = id;
        this.loc = loc;
        this.hologram = hologram;
    }

    public void setHologram(final String s) {
        this.hologram = s;
        final boolean loaded = this.loc.getChunk().isLoaded();
        this.loc.getChunk().load();
        for (final Entity entity : this.loc.getChunk().getEntities()) {
            if (entity.getUniqueId().toString().equalsIgnoreCase(this.id.toString())) {
                entity.setCustomName(VRNUtil.color(s));
            }
        }
        if (!loaded) {
            this.loc.getChunk().unload();
        }
    }

    public void remove() {
        final boolean loaded = this.loc.getChunk().isLoaded();
        this.loc.getChunk().load();
        for (final Entity entity : this.loc.getChunk().getEntities()) {
            if (entity.getUniqueId().toString().equalsIgnoreCase(this.id.toString())) {
                entity.remove();
            }
        }
        if (!loaded) {
            this.loc.getChunk().unload();
        }
    }

    public JsonObject serialize() {
        final JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("id", this.getId().toString());
        jsonObject.addProperty("hologram", this.getHologram());
        jsonObject.addProperty("location", serializeHoloLoc(this.getLoc()));
        return jsonObject;
    }

    public String serializeHoloLoc(final Location location) {
        if (location == null) {
            return null;
        }
        return location.getWorld().getName() + " " + location.getX() + " " + location.getY() + " " + location.getZ();
    }

    public static Location deserializeHoloLoc(final String s) {
        if (s == null) {
            return null;
        }
        final String[] split = s.split(" ");
        if (split.length != 4) {
            return null;
        }
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }
}
