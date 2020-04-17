package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.VRNParticle;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.ReflectionUtil;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.Remain;

public class PlayerTrailTask extends BukkitRunnable {

    @Override
    public void run() {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (PlayerCache.getCache(pl).getPlayertrail() != null) {
                final CompParticle particle = PlayerCache.getCache(pl).getPlayertrail();
                final String perm = VRNParticle.getNameFromParticle(particle);
                if (perm != null) {
                    if (pl.hasPermission("vrn.playertrails." + perm)) {
                        if (particle != CompParticle.REDSTONE) {
                            spawnInt(particle, pl.getLocation(), 3);
                        } else {
                            spawnRedstone(pl.getLocation(), 3);
                        }
                    } else {
                        PlayerCache.getCache(pl).setPlayertrail(null);
                    }
                }
            }
        }
    }

    private void spawnInt(final CompParticle particle, final Location location, final int amount) {
        if (Remain.hasParticleAPI()) {
            final Particle p = ReflectionUtil.lookupEnumSilent(Particle.class, particle.toString());
            if (p != null) {
                location.getWorld().spawnParticle(p, location.clone().add(0, 0.35, 0), amount, 0.3D, 0.3D, 0.3D, 0);
            }
        }
    }

    private void spawnRedstone(final Location location, final int amount) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(0, 0.35, 0), amount, 0.3D, 0.3D, 0.3D, 0, new Particle.DustOptions(Color.RED, 1.0f));
    }
}
