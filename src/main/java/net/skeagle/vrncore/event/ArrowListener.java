package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNParticle;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class ArrowListener implements Listener {

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        if (e.getEntity().getShooter() instanceof Player && e.getEntity() instanceof Arrow) {
            final Particle particle = PlayerManager.getData((Player) e.getEntity().getShooter()).getArrowtrail();
            if (particle != null) {
                final String perm = VRNParticle.getNameFromParticle(particle);
                if (perm != null) {
                    if (((Player) e.getEntity().getShooter()).hasPermission("vrn.arrowtrails." + perm)) {
                        new BukkitRunnable() {
                            @Override
                            public void run() {
                                if (!e.getEntity().isValid() || e.getEntity().isOnGround()) {
                                    cancel();
                                    return;
                                }
                                if (particle != Particle.REDSTONE)
                                    ((Player) e.getEntity().getShooter()).getWorld().spawnParticle(Particle.REDSTONE, e.getEntity().getLocation(),1);
                                else
                                    spawnRedstone(e.getEntity().getLocation());
                            }
                        }.runTaskTimer(VRNcore.getInstance(), 0, 1);
                    } else
                        PlayerManager.getData((Player) e.getEntity().getShooter()).setArrowtrail(null);
                }
            }
        }
    }

    private void spawnRedstone(final Location location) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location, 1, new Particle.DustOptions(Color.RED, 1.0f));
    }
}
