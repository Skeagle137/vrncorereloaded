package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailListener;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Spread extends TrailStyle {

    public Spread() {
        super(Style.SPREAD);
    }

    @Override
    public void tick(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        if (data.getType() == TrailType.PLAYER) {
            if (TrailListener.tick % 8 != 0) return;
            particle.run(player, loc.clone().add(0, 0.6, 0), data, 5, particle.getSpreadSpeed() > 0 ? particle.getSpreadSpeed() : 0.18D, 0, visibility);
        }
        else {
            particle.run(player, loc, data, 3, particle.getSpreadSpeed() > 0 ? particle.getSpreadSpeed() : 0.35D, 0.6D);
        }
    }
}
