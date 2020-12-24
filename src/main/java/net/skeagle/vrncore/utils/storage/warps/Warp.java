package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.api.StoreableObject;
import org.bukkit.Location;

@Getter
public class Warp extends StoreableObject<Warp> {

    private final String name;
    private final Location location;

    Warp(String name, Location location) {
        this.name = name;
        this.location = location;
    }

    public Warp deserialize() {
        return null;
    }

    @Override
    public String serialize() {
        return name + "<>" + VRNUtil.LocationSerialization.serialize(location);
    }
}
