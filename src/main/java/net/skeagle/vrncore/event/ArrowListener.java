package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.CompParticle;

public class ArrowListener implements Listener {

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity() instanceof Arrow) {
                final PlayerCache cache = PlayerCache.getCache(((Player) e.getEntity().getShooter()));
                final CompParticle particle = cache.getArrowtrail();
                if (particle != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!e.getEntity().isValid() || e.getEntity().isOnGround()) {
                                cancel();

                                return;
                            }
                            if (particle != CompParticle.REDSTONE) {
                                particle.spawn(e.getEntity().getLocation());
                            } else {
                                spawnRedstone(e.getEntity().getLocation());
                            }
                        }
                    }.runTaskTimer(VRNcore.getInstance(), 0, 1);
                }
            }
        }
    }

    public final void spawnRedstone(final Location location) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.RED, 1.0f));
    }
}
