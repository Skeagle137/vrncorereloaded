package net.skeagle.vrncore.utils.storage.homes;

import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.api.DBObject;
import org.bukkit.entity.Player;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager extends DBObject<Home> {

    public HomeManager() {
        super("homes", Home.class);
    }

    @Getter
    private static final List<Home> loadedhomes = new ArrayList<>();

    @Getter
    private static final HomeManager instance = new HomeManager();

    public boolean setHome(final String name, Player p) {
        for (Home h : getHomes(p))
            if (name.equalsIgnoreCase(h.getName()))
                return false;
        Home h = new Home(name, p.getUniqueId(), p.getLocation());
        loadedhomes.add(h);
        save(h);
        return true;
    }

    public void importOld(RegisteredHome home, HomesManager man) {
        Home h = new Home(home.getName(), man.getUUID(), home.getLoc());
        getLoadedhomes().add(h);
        save(h);
        loadAllHomes();
    }

    public boolean delHome(final String name, Player p) {
        for (Home h : getHomes(p))
            if (name.equalsIgnoreCase(h.getName())) {
                delete(h);
                loadedhomes.remove(h);
                return true;
            }
        return false;
    }

    public Home getHome(final String name, final Player p) {
        for (Home h : getHomes(p))
            if (h.getName().equalsIgnoreCase(name))
                return h;
        return null;
    }

    public static List<Home> getHomes(Player p) {
        final List<Home> homes = new ArrayList<>();
        for (Home h : loadedhomes)
            if (h.getOwner().equals(p.getUniqueId()))
                homes.add(h);
        return homes;
    }

    public List<String> getHomeNames(Player p) {
        final List<String> names = new ArrayList<>();
        for (Home h : getHomes(p))
            names.add(h.getName());
        return names;
    }

    @Override
    public void save(Home h) {
        try {
            PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() + "(name, owner, location) VALUES (?, ?, ?)");
            ps.setString(1, h.getName());
            ps.setString(2, h.getOwner().toString());
            ps.setString(3, VRNUtil.LocationSerialization.serialize(h.getLocation()));
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();

        }
    }

    @Override
    public void delete(Home h) {
        try {
            PreparedStatement ps = getConn().prepareStatement("DELETE FROM " + getName() + " WHERE name = ? AND owner = ?");
            ps.setString(1, h.getName());
            ps.setString(2, h.getOwner().toString());
            ps.execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onFinishLoad() {
        loadAllHomes();
    }

    public void loadAllHomes() {
        loadedhomes.clear();
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName());
            try (final ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    loadedhomes.add(new Home(rs.getString("name"),
                            UUID.fromString(rs.getString("owner")),
                            VRNUtil.LocationSerialization.deserialize(rs.getString("location"))));
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
