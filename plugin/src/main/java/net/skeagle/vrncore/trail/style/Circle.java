package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.playerdata.TrailData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.util.Vector;

public class Circle extends SplitTrailStyle {

    public Circle(TrailData data) {
        super(data);
    }

    @Override
    public void onPlayerTick(Player player, Location loc) {
        if (step % 2 != 0) return;
        Vector vec = new Vector(0.55, 1.15, 0.55);
        Vector offset = vec.clone().rotateAroundY(Math.toRadians(step * 10));
        run(player, loc.clone().add(offset), 2, 0.001D, 0.001D);
    }

    @Override
    public void onProjectileTick(Projectile projectile, Location loc) {
        Vector dir = loc.getDirection().normalize();
        Vector vec = (Math.abs(dir.getZ()) < Math.abs(dir.getX()) ? new Vector(dir.getY(), -dir.getX(), 0)
                : new Vector(0, -dir.getZ(), dir.getY())).multiply(0.6);
        Vector offset = vec.clone().rotateAroundAxis(dir, Math.toRadians(step * 30));
        run(projectile, loc.clone().add(offset), 3, 0.005D, 0.005D);
    }
}
