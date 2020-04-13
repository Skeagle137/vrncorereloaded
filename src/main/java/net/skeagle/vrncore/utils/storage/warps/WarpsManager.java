package net.skeagle.vrncore.utils.storage.warps;

import lombok.Getter;
import net.skeagle.vrncore.utils.storage.LocationSerialization;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompMaterial;
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

    public CompMaterial getIcon() {
        final Block block_below = loc.clone().add(0, -1, 0).getBlock();
        if (block_below.getType() != CompMaterial.AIR.getMaterial()) {
            return CompMaterial.fromBlock(block_below);
        }
        return CompMaterial.BARRIER;
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

    public String getName() {
        return getFileName().replace(".yml", "");
    }

}