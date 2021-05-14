package net.skeagle.vrncore.utils.storage.homes;

import net.skeagle.vrncore.api.sql.StoreableObject;
import org.bukkit.Location;

import java.util.UUID;

public class Home extends StoreableObject<Home> {

    private final String name;
    private final UUID owner;
    private final Location location;

    Home(final String name, final UUID owner, final Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
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
}
