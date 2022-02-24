package net.skeagle.vrncore.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.LocationUtils;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public record Warp(String name, UUID owner, Location location) {

    public void save(Player p) {
        SQLHelper db = VRNcore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM warps WHERE name = ? AND owner = ?", name, p.getUniqueId());
            db.execute("INSERT INTO warps (name, owner, location) VALUES (?, ?, ?)",
                    name, p.getUniqueId().toString(), LocationUtils.toString(location));
        });
    }
}
