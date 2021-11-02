package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailListener;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

class Orbit extends TrailStyle {

    private int i;
    private int j;

    public Orbit() {
        super(Style.ORBIT);
    }

    @Override
    public void tick(Player player, Particles particle, Location loc, TrailType type, TrailVisibility visibility) {
        if (type == TrailType.PLAYER) {
            if (TrailListener.tick % 3 != 0) return;
            runPlayer(player, particle, loc, visibility, i);
            runPlayer(player, particle, loc, visibility, i + 120);
            runPlayer(player, particle, loc, visibility, i + 240);
        }
        else {
            runArrow(player, particle, loc, j);
            runArrow(player, particle, loc, j + 120);
            runArrow(player, particle, loc, j + 240);
        }
        i += 10 % 360;
        j += 8 % 360;
    }

    private void runPlayer(Player player, Particles particle, Location loc, TrailVisibility visibility, int i) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(i)) * 0.98, 0.85, Math.sin(Math.toRadians(i)) * 0.98), 2, 0.008D, 0.05D, visibility);
    }

    private void runArrow(Player player, Particles particle, Location loc, int j) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(j)) * 0.85, 0, Math.sin(Math.toRadians(j)) * 0.85), 2, 0.008D, 0.05D, TrailVisibility.ALL);
    }
}
