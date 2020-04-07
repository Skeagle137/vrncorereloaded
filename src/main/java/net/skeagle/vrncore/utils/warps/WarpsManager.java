package net.skeagle.vrncore.utils.warps;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class WarpsManager extends YamlConfig {

    WarpsManager(final String name) {
        setHeader();
        loadConfiguration(null, "warps/" + name.toLowerCase() + (!name.toLowerCase().endsWith(".yml") ? ".yml" : ""));
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

        save("Location", loc);
    }

    @Override
    protected void onLoadFinish() {
        if (isSet("Location")) {
            loc = getLocation("Location");
        }
    }

    String getName() {
        return getFileName().replace(".yml", "");
    }

}