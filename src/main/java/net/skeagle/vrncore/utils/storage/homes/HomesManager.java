package net.skeagle.vrncore.utils.storage.homes;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Getter
public class HomesManager extends YamlConfig {

    public List<RegisteredHome> homes = new ArrayList<>();

    HomesManager(final UUID uuid) {
        setHeader("");
        loadConfiguration(null, "homes/" + uuid.toString() + (!uuid.toString().endsWith(".yml") ? ".yml" : ""));
    }

    public boolean setHome(final String name, final Location loc) {
        for (final RegisteredHome home : homes) {
            if (name.equalsIgnoreCase(home.getName())) {
                return false;
            }
        }
        final RegisteredHome newHome = new RegisteredHome(name, loc);
        homes.add(newHome);
        save(name, loc.serialize());
        return true;
    }

    public boolean delHome(final String name) {
        for (final RegisteredHome home : homes) {
            if (name.equalsIgnoreCase(home.getName())) {
                save(name, null);
                homes.remove(home);
                return true;
            }
        }
        return false;
    }

    public void delHome(final RegisteredHome home) {
        save(home.getName(), null);
        homes.remove(home);
    }

    public boolean teleHome(final Player p, final String name) {
        for (final RegisteredHome home : homes) {
            if (name.equalsIgnoreCase(home.getName())) {
                p.teleport(home.getLoc());
                return true;
            }
        }
        return false;
    }

	/*
	public boolean delAllHomes() {
		if (homes.size() != 0) {
			homes.clear();
			delete();
			return true;
		}
		return false;
	}
	*/

    public List<String> homeNames() {
        final Map<String, Object> map = getConfig().getValues(false);
        return new ArrayList<>(map.keySet());
    }


    @Override
    protected void onLoadFinish() {
        homes.clear();
        for (final String s : getConfig().getKeys(false)) {
            final Map<String, Object> locData = getConfig().getConfigurationSection(s).getValues(true);
            homes.add(new RegisteredHome(s, Location.deserialize(locData)));
        }
    }

    UUID getUUID() {
        return UUID.fromString(getFileName().replace(".yml", ""));
    }

}
