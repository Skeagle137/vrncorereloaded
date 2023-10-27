package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.playerdata.TrailData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

public class Default extends SplitTrailStyle {

    public Default(TrailData data) {
        super(data);
    }

    @Override
    public void onPlayerTick(Player player, Location loc) {
        if (step % 3 != 0) return;
        run(player, loc.clone().add(0, 0.3, 0), 3, 0.001D, 0.2D);
    }

    @Override
    public void onProjectileTick(Projectile projectile, Location loc) {
        run(projectile, loc, 2, 0.1D, 0.02D);
    }
}
