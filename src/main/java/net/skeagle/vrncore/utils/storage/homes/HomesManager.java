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
