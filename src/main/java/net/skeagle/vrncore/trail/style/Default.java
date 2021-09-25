package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailListener;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class Default extends TrailStyle {

    public Default() {
        super(Style.DEFAULT);
    }

    @Override
    public void tick(Player player, Particles particle, Location loc, TrailType type, TrailVisibility visibility) {
        if (type == TrailType.PLAYER) {
            if (TrailListener.tick % 3 != 0) return;
            particle.run(player, loc.clone().add(0, 0.3, 0), 3, 0.001D, 0.2D, visibility);
        }
        else {
            particle.run(player, loc, 2, 0.1D, 0.02D);
        }
    }
}
