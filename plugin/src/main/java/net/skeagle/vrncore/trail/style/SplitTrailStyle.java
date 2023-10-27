package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.TrailType;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

abstract class SplitTrailStyle extends TrailStyle {

    public SplitTrailStyle(TrailData data) {
        super(data);
    }

    @Override
    public void onTick(Entity target, Location loc) {
        if (data.getType() == TrailType.PLAYER) {
            onPlayerTick((Player) target, loc);
        }
        else {
            onProjectileTick((Projectile) target, loc);
        }
    }

    public abstract void onPlayerTick(Player player, Location loc);
    public abstract void onProjectileTick(Projectile projectile, Location loc);
}
