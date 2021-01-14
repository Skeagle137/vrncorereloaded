package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.api.DBObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class WarpManager extends DBObject<Warp> {

    public WarpManager() {
        super("warps", Warp.class);
    }

    @Getter
    private static final List<Warp> loadedwarps = new ArrayList<>();

    @Getter
    private static final WarpManager instance = new WarpManager();

    public boolean setWarp(final Player p, final String name) {
        for (Warp w : loadedwarps)
            if (w.getName().equalsIgnoreCase(name))
                return false;

        Warp w = new Warp(name, p.getLocation());
        save(w);
        loadedwarps.add(w);
        return true;
    }

    public boolean delWarp(final String name) {
        for (Warp w : loadedwarps)
            if (w.getName().equalsIgnoreCase(name)) {
                delete(w);
                loadedwarps.remove(w);
                return true;
            }
        return false;
    }

    public Warp getWarp(final String name) {
        for (Warp w : loadedwarps)
            if (w.getName().equalsIgnoreCase(name))
                return w;
        return null;
    }

    public List<String> getWarpNames() {
        final List<String> names = new ArrayList<>();
        for (Warp w : loadedwarps)
            names.add(w.getName());
        return names;
    }

    @Override
    public void save(Warp w) {
        try {
            PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() + "(name, location) VALUES (?, ?)");
            ps.setString(1, w.getName());
            ps.setString(2, VRNUtil.LocationSerialization.serialize(w.getLocation()));
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(Warp w) {
        try {
            PreparedStatement ps = getConn().prepareStatement("DELETE FROM " + getName() + " WHERE name = ?");
            ps.setString(1, w.getName());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishLoad() {
        loadAllWarps();
    }

    public void loadAllWarps() {
        loadedwarps.clear();
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName());
            try (final ResultSet rs = ps.executeQuery()) {
                Location loc;
                while (rs.next()) {
                    loc = VRNUtil.LocationSerialization.deserialize(rs.getString("location"));
                    if (loc == null) {
                        Common.log("Warning, location is null for warp id " + rs.getInt("id"));
                        continue;
                    }
                    loadedwarps.add(new Warp(rs.getString("name"), loc));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
