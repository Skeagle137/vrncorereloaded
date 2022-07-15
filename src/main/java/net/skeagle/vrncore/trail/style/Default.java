package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailHandler;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Default extends TrailStyle {

    public Default() {
        super(Style.DEFAULT);
    }

    @Override
    public void tick(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        if (data.getType() == TrailType.PLAYER) {
            if (TrailHandler.tick % 3 != 0) return;
            particle.run(player, loc.clone().add(0, 0.3, 0), data, 3, 0.001D, 0.2D, visibility);
        }
        else {
            particle.run(player, loc, data, 2, 0.1D, 0.02D);
        }
    }
}
