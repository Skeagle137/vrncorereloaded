package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.remain.CompParticle;

public class ArrowListener implements Listener {

    @EventHandler
    public void onArrowShot(ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player) {
            if (e.getEntity() instanceof Arrow) {
                PlayerCache cache = PlayerCache.getCache(((Player) e.getEntity().getShooter()));
                CompParticle particle = cache.getArrowtrail();
                if (particle != null) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!e.getEntity().isValid() || e.getEntity().isOnGround()) {
                                cancel();

                                return;
                            }
                            particle.spawn(e.getEntity().getLocation());
                        }
                    }.runTaskTimer(VRNcore.getInstance(), 0, 1);
                }
            }
        }
    }
}
