package net.skeagle.vrncore.utils.storage.homes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;

@Getter
@Setter
public class RegisteredHome {

    private String name;
    private Location loc;

    RegisteredHome(final String name, final Location loc) {
        this.name = name;
        this.loc = loc;
    }
}
