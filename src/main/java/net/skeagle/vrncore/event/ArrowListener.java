package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.trail.Trail;
import net.skeagle.vrncore.utils.VRNPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Locale;

public class ArrowListener implements Listener {

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        final VRNPlayer vrnPlayer;
        if (e.getEntity().getShooter() instanceof Player player && e.getEntity() instanceof Arrow) {
            vrnPlayer = new VRNPlayer(player);
            final Trail trail = Trail.getFromParticle(vrnPlayer.getArrowTrail());
            if (trail != null) {
                if (((Player) e.getEntity().getShooter()).hasPermission("vrn.arrowtrails." + trail.toString().toLowerCase(Locale.ROOT))) {
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            if (!e.getEntity().isValid() || e.getEntity().isOnGround()) {
                                cancel();
                                return;
                            }
                            trail.run(player, e.getEntity().getLocation(), 2, 0.1D, 0.02D);
                        }
                    }.runTaskTimer(VRNcore.getInstance(), 2, 1);
                } else
                    vrnPlayer.setArrowTrail(null);
            }
        }
    }
}
