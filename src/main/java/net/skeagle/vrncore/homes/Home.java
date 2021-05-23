package net.skeagle.vrncore.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Home {

    private final String name;
    private final UUID owner;
    private final Location location;

    Home(final String name, final UUID owner, final Location location) {
        this.name = name;
        this.owner = owner;
        this.location = location;
    }

    public void save(final String name, final Player p) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM homes WHERE name = ? AND owner = ?", name, p.getUniqueId());
            db.execute("INSERT INTO homes (name, owner, location) VALUES (?, ?, ?)",
                    name, p.getUniqueId().toString(), VRNUtil.LocationSerialization.serialize(location));
        });
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
