package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.trail.VRNParticle;
import net.skeagle.vrncore.utils.VRNPlayer;
import org.bukkit.Color;
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
        final VRNPlayer vrnPlayer;
        if (e.getEntity().getShooter() instanceof Player && e.getEntity() instanceof Arrow) {
            vrnPlayer = new VRNPlayer((Player) e.getEntity().getShooter());
            final Particle particle = vrnPlayer.getArrowTrail();
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
                                ((Player) e.getEntity().getShooter()).getWorld().spawnParticle(particle, e.getEntity().getLocation(),
                                        2, 0.1D, 0.1D, 0.1D, 0.02D, particle == Particle.REDSTONE ? new Particle.DustOptions(Color.RED, 1.0f) : null);
                            }
                        }.runTaskTimer(VRNcore.getInstance(), 2, 1);
                    } else {
                        vrnPlayer.setArrowTrail(null);
                    }
                }
            }
        }
    }
}
