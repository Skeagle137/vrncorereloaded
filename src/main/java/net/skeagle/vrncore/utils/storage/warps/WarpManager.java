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
import java.util.UUID;

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

        Warp w = new Warp(name, p.getLocation(), p.getUniqueId());
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

    public void updateWarpOwner(Warp w, UUID uuid) {
        try {
            PreparedStatement ps = getConn().prepareStatement("UPDATE " + getName() + "SET uuid = " + uuid.toString() + " WHERE name = " + w.getName());
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Warp> getWarpsOwnedByPlayer(Player p) {
        final List<Warp> owned_warps = new ArrayList<>();
        for (Warp w : loadedwarps)
            if (w.getOwner().equals(p.getUniqueId()))
                owned_warps.add(w);
        return owned_warps;
    }

    @Override
    public void save(Warp w) {
        try {
            PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() + "(name, location, owner) VALUES (?, ?, ?)");
            ps.setString(1, w.getName());
            ps.setString(2, VRNUtil.LocationSerialization.serialize(w.getLocation()));
            ps.setString(3, w.getOwner().toString());
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
                        Common.log("Location is null for warp name " + rs.getString("name"));
                        continue;
                    }
                    loadedwarps.add(new Warp(rs.getString("name"), loc, UUID.fromString(rs.getString("owner"))));
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
