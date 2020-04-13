package net.skeagle.vrncore.utils.storage.homes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.mineacademy.fo.remain.CompMaterial;

@Getter
@Setter
public class RegisteredHome {

    private String name;
    private Location loc;

    public RegisteredHome(final String name, final Location loc) {
        this.name = name;
        this.loc = loc;
    }


    public CompMaterial getIcon() {
        final Block block_below = loc.clone().add(0, -1, 0).getBlock();
        if (block_below.getType() != CompMaterial.AIR.getMaterial()) {
            return CompMaterial.fromBlock(block_below);
        }
        return CompMaterial.BARRIER;
    }
}
