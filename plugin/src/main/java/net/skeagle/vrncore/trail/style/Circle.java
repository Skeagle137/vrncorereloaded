package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailHandler;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Circle extends TrailStyle {

    private int i;
    private int j;

    public Circle() {
        super(Style.CIRCLE);
    }

    @Override
    public void tick(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        if (data.getType() == TrailType.PLAYER) {
            if (TrailHandler.tick % 2 != 0) return;
            runPlayer(player, loc, data, particle, visibility, i);
        }
        else {
            runArrow(player, loc, data, particle, j);
        }
        i += 20 % 360;
        j += 35 % 360;
    }

    private void runPlayer(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility, int i) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(i)) * 0.6, 1.15, Math.sin(Math.toRadians(i)) * 0.6), data, 2, 0.001D, 0.001D, visibility);
    }

    private void runArrow(Player player, Location loc, TrailData data, Particles particle, int j) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(j)) * 0.6, 0, Math.sin(Math.toRadians(j)) * 0.6), data, 3, 0.005D, 0.005D, TrailVisibility.ALL);
    }
}
