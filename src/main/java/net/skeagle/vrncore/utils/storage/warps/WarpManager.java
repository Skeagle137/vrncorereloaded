package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.api.sql.DBObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    private static final WarpManager instance = new WarpManager();

    public boolean setWarp(final Player p, final String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE name = '" + name + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Warp w = new Warp(name, p.getLocation(), p.getUniqueId());
        save(w);
        return true;
    }

    public boolean delWarp(final String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE name = '" + name + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                delete(name);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Warp getWarp(final String name) {
        try {
            System.out.println("no");
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE name = '" + name + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                System.out.println("no");
                return new Warp(rs.getString("name"),
                        VRNUtil.LocationSerialization.deserialize(rs.getString("location")),
                        UUID.fromString(rs.getString("owner")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Location getLocationFromName(final String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT location FROM " + getName() + " WHERE name = '" + name + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next())
                return VRNUtil.LocationSerialization.deserialize(rs.getString("location"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getWarpNames() {
        final List<String> names = new ArrayList<>();
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT name FROM " + getName());
            final ResultSet rs = ps.executeQuery();
            while (rs.next())
                names.add(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public List<String> getWarpsOwnedByPlayer(Player p) {
        final List<String> owned_warps = new ArrayList<>();
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            while (rs.next())
                owned_warps.add(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return owned_warps;
    }

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

    public void delete(String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("DELETE FROM " + getName() + " WHERE name = '" + name + "'");
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
