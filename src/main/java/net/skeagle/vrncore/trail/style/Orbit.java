package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.event.TrailHandler;
import net.skeagle.vrncore.playerdata.TrailData;
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
    public void tick(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        if (data.getType() == TrailType.PLAYER) {
            if (TrailHandler.tick % 3 != 0) return;
            runPlayer(player, loc, data, particle, visibility, i);
            runPlayer(player, loc, data, particle, visibility, i + 120);
            runPlayer(player, loc, data, particle, visibility, i + 240);
        }
        else {
            runArrow(player, loc, data, particle, j);
            runArrow(player, loc, data, particle, j + 120);
            runArrow(player, loc, data, particle, j + 240);
        }
        i += 10 % 360;
        j += 8 % 360;
    }

    private void runPlayer(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility, int i) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(i)) * 0.98, 0.85, Math.sin(Math.toRadians(i)) * 0.98), data, 2, 0.008D, 0.05D, visibility);
    }

    private void runArrow(Player player, Location loc, TrailData data, Particles particle, int j) {
        particle.run(player, loc.clone().add(Math.cos(Math.toRadians(j)) * 0.85, 0, Math.sin(Math.toRadians(j)) * 0.85), data, 2, 0.008D, 0.05D, TrailVisibility.ALL);
    }
}
