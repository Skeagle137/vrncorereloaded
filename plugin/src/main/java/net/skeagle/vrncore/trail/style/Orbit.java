package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.playerdata.TrailData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class Orbit extends SplitTrailStyle {

    public Orbit(TrailData data) {
        super(data);
    }

    @Override
    public void onPlayerTick(Player player, Location loc) {
        if (step % 3 != 0) return;
        Vector vec = new Vector(0.7, 0.85, 0.7);
        for (double i = 0; i < 3; i++) {
            Vector offset = vec.clone().rotateAroundY(Math.toRadians((i * 120) + (step * 2)));
            run(player, loc.clone().add(offset), 2, 0.008D, 0.05D);
        }
    }

    @Override
    public void onProjectileTick(Projectile projectile, Location loc) {
        Vector dir = loc.getDirection().normalize();
        Vector vec = (Math.abs(dir.getZ()) < Math.abs(dir.getX()) ? new Vector(dir.getY(), -dir.getX(), 0)
                : new Vector(0, -dir.getZ(), dir.getY())).multiply(0.85);
        for (double i = 0; i < 3; i++) {
            Vector offset = vec.clone().rotateAroundAxis(dir, Math.toRadians((i * 120) + (step * 10)));
            run(projectile, loc.clone().add(offset), 2, 0.008D, 0.05D);
        }
    }
}
