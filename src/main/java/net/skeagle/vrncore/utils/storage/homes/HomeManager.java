package net.skeagle.vrncore.utils.storage.homes;

import net.skeagle.vrncore.api.sql.SQLConnection;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.api.sql.DBObject;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager extends DBObject<Home> {

    public HomeManager() {
        super("homes", Home.class);
    }

    private static final HomeManager instance = new HomeManager();

    private static final List<String> homes_map = new ArrayList<>();

    public boolean setHome(final String name, Player p) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT location FROM " + getName() + " WHERE name = '" + name + "' AND owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next())
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        save(name, p);
        return true;
    }

    public boolean delHome(final String name, Player p) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT location FROM " + getName() + " WHERE name = '" + name + "' AND owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                delete(p, name);
                return true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public Home getHome(final String name, final Player p) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE name = '" + name + "' AND owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next())
                return new Home(rs.getString("name"),
                        UUID.fromString(rs.getString("owner")),
                        VRNUtil.LocationSerialization.deserialize(rs.getString("location")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public Location getLocationFromName(final Player p, final String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT location FROM " + getName() + " WHERE name = '" + name + "' AND owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            if (rs.next())
                return VRNUtil.LocationSerialization.deserialize(rs.getString("location"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<String> getHomeNames(Player p) {
        final List<String> names = new ArrayList<>();
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT name FROM " + getName() + " WHERE owner = '" + p.getUniqueId() + "'");
            final ResultSet rs = ps.executeQuery();
            while (rs.next())
                names.add(rs.getString("name"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return names;
    }

    public void save(String name, Player p) {
        Common.runAsync(() -> {
            try {
                PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() + "(name, owner, location) VALUES (?, ?, ?)");
                ps.setString(1, name);
                ps.setString(2, p.getUniqueId().toString());
                ps.setString(3, VRNUtil.LocationSerialization.serialize(p.getLocation()));
                ps.execute();
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public void delete(Player p, String name) {
        try {
            PreparedStatement ps = getConn().prepareStatement("DELETE FROM " + getName() + " WHERE name = '" + name + "' AND owner = '" + p.getUniqueId() + "'");
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static HomeManager getInstance() {
        return instance;
    }
}
