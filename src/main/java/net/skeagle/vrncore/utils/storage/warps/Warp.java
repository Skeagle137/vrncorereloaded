package net.skeagle.vrncore.utils.storage.warps;

import net.skeagle.vrncore.api.sql.StoreableObject;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Location;

import java.util.UUID;

public class Warp extends StoreableObject<Warp> {

    private final String name;
    private final Location location;
    private UUID owner;

    Warp(final String name, final Location location, final UUID owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    @Override
    public Warp deserialize() {
        return null;
    }

    @Override
    public String serialize() {
        return name + "<>" + VRNUtil.LocationSerialization.serialize(location);
    }

    public String getName() {
        return name;
    }

    public Location getLocation() {
        return location;
    }

    public UUID getOwner() {
        return owner;
    }

    public void setOwner(final UUID owner) {
        this.owner = owner;
    }
}
