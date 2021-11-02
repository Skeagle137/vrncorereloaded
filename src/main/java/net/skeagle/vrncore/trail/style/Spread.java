package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailListener;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Spread extends TrailStyle {

    public Spread() {
        super(Style.SPREAD);
    }

    @Override
    public void tick(Player player, Particles particle, Location loc, TrailType type, TrailVisibility visibility) {
        if (type == TrailType.PLAYER) {
            if (TrailListener.tick % 8 != 0) return;
            particle.run(player, loc.clone().add(0, 0.6, 0), 6, 0.15D, 0.1D, visibility);
        }
        else {
            particle.run(player, loc, 3, 0.2D, 0.2D);
        }
    }
}
