package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Location;
import org.mineacademy.fo.settings.YamlConfig;

@Getter
public class WarpsManager extends YamlConfig {

    WarpsManager(final String name) {
        setHeader();
        loadConfiguration(null, "warps/" + name + (!name.toLowerCase().endsWith(".yml") ? ".yml" : ""));
    }

    Location loc;

    @Override
    protected void onLoadFinish() {
        if (isSet("Location"))
            loc = VRNUtil.LocationSerialization.deserialize(getString("Location"));
    }

    public String getName() {
        return getFileName().replace(".yml", "");
    }

}