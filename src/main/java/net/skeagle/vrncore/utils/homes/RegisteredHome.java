package net.skeagle.vrncore.utils.homes;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
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


    public CompMaterial getIcon(final Location loc) {
        if (loc.getBlock().getType() != CompMaterial.AIR.getMaterial()) {
            return CompMaterial.fromBlock(loc.getBlock());
        }
        return CompMaterial.BARRIER;
    }
}
