package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import lombok.Setter;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.api.sql.StoreableObject;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Warp extends StoreableObject<Warp> {

    private final String name;
    private final Location location;
    @Setter
    private UUID owner;

    Warp(String name, Location location, UUID owner) {
        this.name = name;
        this.location = location;
        this.owner = owner;
    }

    public Warp deserialize() {
        return null;
    }

    @Override
    public String serialize() {
        return name + "<>" + VRNUtil.LocationSerialization.serialize(location);
    }
}
