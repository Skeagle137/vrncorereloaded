package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;

import java.util.Arrays;

public class Spread extends SplitTrailStyle {

    public Spread(TrailData data) {
        super(data);
    }

    @Override
    public void onPlayerTick(Player player, Location loc) {
        if (step % 8 != 0) return;
        run(player, loc.clone().add(0, 0.6, 0), 5, particle.getSpreadSpeed() > 0 ? particle.getSpreadSpeed() : 0.18D, 0);
    }

    @Override
    public void onProjectileTick(Projectile projectile, Location loc) {
        run(projectile, loc, 3, particle.getSpreadSpeed() > 0 ? particle.getSpreadSpeed() : 0.35D, 0.6D);
    }

    @Override
    public boolean canApply(Particles particle) {
        return Arrays.asList(particle.getProperties()).contains(Particles.ParticleProperties.DIRECTIONAL);
    }
}
