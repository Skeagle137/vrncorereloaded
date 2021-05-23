package net.skeagle.vrncore.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class WarpManager {

    private final List<Warp> warps;

    public WarpManager() {
        warps = new ArrayList<>();
        load();
    }

    public void load() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM warps");
        res.forEach(warp -> {
            final String name = warp.getString(2);
            final UUID owner = UUID.fromString(warp.getString(3));
            final Location loc = VRNUtil.LocationSerialization.deserialize(warp.getString(4));
            warps.add(new Warp(name, owner, loc));
        });
    }

    public void createWarp(final Player p, final String name) {
        final Warp warp = new Warp(name, p.getUniqueId(), p.getLocation());
        warps.add(warp);
        warp.save(name, p);
    }

    public Warp getWarp(final String name) {
        return warps.stream().filter(w -> w.getName().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public List<String> getWarpNames() {
        final List<String> names = new ArrayList<>();
        warps.forEach(w -> names.add(w.getName()));
        return names;
    }

    public void deleteWarp(final Warp warp) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM warps WHERE name = ? AND owner = ?", warp.getName(), warp.getOwner());
        warps.remove(warp);
    }

    public List<Warp> getWarps() {
        return warps;
    }
}
