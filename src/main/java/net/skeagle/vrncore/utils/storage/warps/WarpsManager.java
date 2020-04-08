package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.utils.storage.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class WarpsManager extends YamlConfig {

    WarpsManager(final String name) {
        setHeader();
        loadConfiguration(null, "warps/" + name + (!name.toLowerCase().endsWith(".yml") ? ".yml" : ""));
    }

    Location loc;

    public boolean teleWarp(final Player p) {
        if (isSet("Location")) {
            p.teleport(loc);
            return true;
        }
        return false;
    }

    void setLoc(final Location loc) {
        this.loc = loc;

        save("Location", LocationSerialization.serialize(loc));
    }

    @Override
    protected void onLoadFinish() {
        if (isSet("Location")) {
            loc = LocationSerialization.deserialize(getString("Location"));
        }
    }

    String getName() {
        return getFileName().replace(".yml", "");
    }

}