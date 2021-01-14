package net.skeagle.vrncore.utils.storage.homes;

import lombok.Getter;
import net.skeagle.vrncore.utils.storage.api.StoreableObject;
import org.bukkit.Location;

import java.util.UUID;

@Getter
public class Home extends StoreableObject<Home> {

    private final String name;
    private final UUID owner;
    private final Location location;

    Home(String name, UUID owner, Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }
}
