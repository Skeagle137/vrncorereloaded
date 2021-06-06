package net.skeagle.vrncore.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.commandmanager.ArgType;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class HomeManager {

    private final List<Home> homes;

    public HomeManager() {
        homes = new ArrayList<>();
        load();
    }

    public void load() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM homes");
        res.forEach(home -> {
            final String name = home.getString(2);
            final UUID owner = UUID.fromString(home.getString(3));
            final Location loc = VRNUtil.LocationSerialization.deserialize(home.getString(4));
            homes.add(new Home(name, owner, loc));
        });
    }

    public void createHome(final String name, final Player p) {
        final Home home = new Home(name, p.getUniqueId(), p.getLocation());
        homes.add(home);
        home.save(name, p);
    }

    public void deleteHome(final Home home) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM homes WHERE name = ? AND owner = ?", home.getName(), home.getOwner());
        homes.remove(home);
    }

    public Home getHome(final String name, final Player p) {
        return homes.stream().filter(h -> h.getName().equalsIgnoreCase(name) && h.getOwner().equals(p.getUniqueId())).findFirst().orElse(null);
    }

    public List<String> getHomeNames(final Player p) {
        final List<String> names = new ArrayList<>();
        homes.stream().filter(h -> h.getOwner().equals(p.getUniqueId())).forEach(h -> names.add(h.getName()));
        return names;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public ArgType<Home> getArgType() {
        return new ArgType<>("home", (s, c) -> this.getHome(c, (Player) s)).setTab((s, c) -> getHomeNames((Player) s));
    }
}
