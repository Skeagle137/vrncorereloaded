package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Orbit extends TrailStyle {

    public Orbit() {
        super(Style.ORBIT);
    }

    @Override
    public void tick(Player player, Particles particle, Location loc, TrailType type, TrailVisibility visibility) {

    }
}
