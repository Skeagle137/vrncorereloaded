package net.skeagle.vrncore.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public record Home(String name, UUID owner, Location location) {

    public void save(Player p) {
        SQLHelper db = VRNcore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM homes WHERE name = ? AND owner = ?", name, p.getUniqueId());
            db.execute("INSERT INTO homes (name, owner, location) VALUES (?, ?, ?)",
                    name, p.getUniqueId().toString(), VRNUtil.LocationSerialization.serialize(location));
        });
    }
}
