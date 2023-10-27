package net.skeagle.vrncore.trail.style.idle;

import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.style.TrailStyle;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.util.Vector;

abstract class Star extends TrailStyle implements IdleStyle {

    private final int skip, vertices;
    private final double speed, size, angle;
    private double offset;

    public Star(TrailData data, int skip, double speed, int vertices, double size) {
        super(data);
        this.skip = skip;
        this.speed = speed;
        this.vertices = vertices;
        this.size = size;
        this.angle = 360f / vertices;
    }

    @Override
    public void onTick(Entity target, Location loc) {
        if (step % skip != 0) return;
        for (int i = 0; i < vertices; i++) {
            double pointFrom = Math.toRadians(i + (i * angle) + offset);
            double pointTo = Math.toRadians(i + ((i + 2) * angle) + offset);
            Vector from = new Vector(Math.cos(pointFrom) * size, 0, Math.sin(pointFrom) * size);
            Vector to = new Vector(Math.cos(pointTo) * size, 0, Math.sin(pointTo) * size);
            Vector dist = to.subtract(from);
            for (double j = 0.001; j <= from.distance(to) + 1; j += 0.5) {
                dist.multiply(j);
                from.add(dist);
                run(target, loc.clone().add(from.getX(), 0.1, from.getZ()), 1, 0.008D, 0.05D);
                from.subtract(dist);
                dist.normalize();
            }
        }
        offset += speed % angle;
    }
}
