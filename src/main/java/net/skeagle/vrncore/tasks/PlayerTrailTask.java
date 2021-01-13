package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNParticle;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerTrailTask extends BukkitRunnable {

    public PlayerTrailTask(VRNcore vrn) {
        runTaskTimer(vrn, 0, 3);
    }

    @Override
    public void run() {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!PlayerManager.getData(pl).getVanished()) {
                if (PlayerManager.getData(pl).getPlayertrail() != null) {
                    final Particle particle = PlayerManager.getData(pl).getPlayertrail();
                    final String perm = VRNParticle.getNameFromParticle(particle);
                    if (perm != null) {
                        if (pl.hasPermission("vrn.playertrails." + perm)) {
                            if (particle != Particle.REDSTONE)
                                pl.getLocation().getWorld().spawnParticle(particle, pl.getLocation().clone().add(0, 0.35, 0), 3, 0.3D, 0.3D, 0.3D, 0);
                            else
                                spawnRedstone(pl.getLocation(), 3);
                        } else
                            PlayerManager.getData(pl).setPlayertrail(null);
                    }
                }
            }
        }
    }

    private void spawnRedstone(final Location location, final int amount) {
        location.getWorld().spawnParticle(Particle.REDSTONE, location.clone().add(0, 0.35, 0), amount, 0.3D, 0.3D, 0.3D, 0, new Particle.DustOptions(Color.RED, 1.0f));
    }
}
