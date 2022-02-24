package net.skeagle.vrncore.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.LocationUtils;
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
        SQLHelper db = VRNcore.getInstance().getDB();
        SQLHelper.Results res = db.queryResults("SELECT * FROM warps");
        res.forEach(warp -> {
            String name = warp.getString(2);
            UUID owner = UUID.fromString(warp.getString(3));
            Location loc = LocationUtils.fromString(warp.getString(4));
            warps.add(new Warp(name, owner, loc));
        });
    }

    public void createWarp(Player p, String name) {
        Warp warp = new Warp(name, p.getUniqueId(), p.getLocation());
        warps.add(warp);
        warp.save(p);
    }

    public Warp getWarp(String name) {
        return warps.stream().filter(w -> w.name().equalsIgnoreCase(name)).findFirst().orElse(null);
    }

    public long getWarpsOwned(Player player) {
        return warps.stream().filter(w -> w.owner().equals(player.getUniqueId())).count();
    }

    public void deleteWarp(Warp warp) {
        SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM warps WHERE name = ? AND owner = ?", warp.name(), warp.owner());
        warps.remove(warp);
    }

    public List<Warp> getWarps() {
        return warps;
    }
}
