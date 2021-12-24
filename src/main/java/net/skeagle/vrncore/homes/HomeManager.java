package net.skeagle.vrncore.homes;

import net.skeagle.vrncommands.ArgType;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
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
        SQLHelper db = VRNcore.getInstance().getDB();
        SQLHelper.Results res = db.queryResults("SELECT * FROM homes");
        res.forEach(home -> {
            String name = home.getString(2);
            UUID owner = UUID.fromString(home.getString(3));
            Location loc = VRNUtil.LocationSerialization.deserialize(home.getString(4));
            homes.add(new Home(name, owner, loc));
        });
    }

    public void createHome(String name, Player p) {
        Home home = new Home(name, p.getUniqueId(), p.getLocation());
        homes.add(home);
        home.save(p);
    }

    public void deleteHome(Home home) {
        SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM homes WHERE name = ? AND owner = ?", home.name(), home.owner());
        homes.remove(home);
    }

    public Home getHome(String name, Player p) {
        return homes.stream().filter(h -> h.name().equalsIgnoreCase(name) && h.owner().equals(p.getUniqueId())).findFirst().orElse(null);
    }

    public List<String> getHomeNames(Player p) {
        List<String> names = new ArrayList<>();
        homes.stream().filter(h -> h.owner().equals(p.getUniqueId())).forEach(h -> names.add(h.name()));
        return names;
    }

    public List<Home> getHomes() {
        return homes;
    }

    public ArgType<Home> getArgType() {
        return new ArgType<>("home", (s, c) -> getHome(c, (Player) s)).setTab((s, c) -> getHomeNames((Player) s));
    }
}
