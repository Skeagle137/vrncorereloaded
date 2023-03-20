package net.skeagle.vrncore.trail.style.idle;

import net.skeagle.vrncore.event.TrailHandler;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailVisibility;
import net.skeagle.vrncore.trail.style.TrailStyle;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

abstract class BaseStar extends TrailStyle implements IdleStyle {

    private final int skip;
    private final double speed;
    private final int vertices;
    private final double size;
    private final double angle;
    private double offset;

    public BaseStar(Style style, int skip, double speed, int vertices, double size) {
        super(style);
        this.skip = skip;
        this.speed = speed;
        this.vertices = vertices;
        this.size = size;
        this.angle = 360f / vertices;
    }

    @Override
    public void tick(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        if (TrailHandler.tick % skip != 0) return;
        for (int i = 0; i < vertices; i++) {
            double pointFrom = Math.toRadians(i + (i * angle) + offset);
            double pointTo = Math.toRadians(i + ((i + 2) * angle) + offset);
            Vector from = new Vector(Math.cos(pointFrom) * size, 0, Math.sin(pointFrom) * size);
            Vector to = new Vector(Math.cos(pointTo) * size, 0, Math.sin(pointTo) * size);
            Vector dist = to.subtract(from);
            for (double j = 0.001; j <= from.distance(to) + 1; j += 0.5) {
                dist.multiply(j);
                from.add(dist);
                this.runPlayer(player, loc.clone().add(from.getX(), 0.1, from.getZ()), data, particle, visibility);
                from.subtract(dist);
                dist.normalize();
            }
        }
        offset += speed % angle;
    }

    private void runPlayer(Player player, Location loc, TrailData data, Particles particle, TrailVisibility visibility) {
        particle.run(player, loc, data, 1, 0.008D, 0.05D, visibility);
    }
}
