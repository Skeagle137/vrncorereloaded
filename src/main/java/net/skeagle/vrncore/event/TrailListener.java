package net.skeagle.vrncore.event;

import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;

public class TrailListener implements Listener {

    private Task trailTask;
    public static int tick = 0;

    public TrailListener() {
        this.startParticleTask();
    }

    public void startParticleTask() {
        if (trailTask != null) {
            trailTask.cancel();
        }

        trailTask = Task.syncRepeating(() -> {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                final PlayerData data = PlayerManager.getData(pl.getUniqueId());
                final Particles trail = Particles.getFromParticle(data.getPlayerTrail());
                if (trail == null) {
                    continue;
                }
                if (pl.hasPermission(trail.getPermission(TrailType.PLAYER))) {
                    data.getTrailStyle().tick(pl, trail, pl.getLocation(), TrailType.PLAYER, data.getStates().isVanished() ? TrailVisibility.CLIENT : TrailVisibility.ALL);
                    continue;
                }
                data.setPlayerTrail(null);
            }
            tick = ++tick % 60;
        }, 20L, 1L);
    }

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player player) || !(e.getEntity() instanceof Arrow arrow)) return;
        final PlayerData data = PlayerManager.getData(player.getUniqueId());
        final Particles trail = Particles.getFromParticle(data.getArrowTrail());
        if (trail == null) {
            return;
        }
        if (player.hasPermission(trail.getPermission(TrailType.ARROW))) {
            Task.syncRepeating(run -> {
                if (!arrow.isValid() || arrow.isOnGround()) {
                    run.cancel();
                    return;
                }
                data.getTrailStyle().tick(player, trail, arrow.getLocation(), TrailType.ARROW, TrailVisibility.ALL);
            }, 2, 1);
            return;
        }
        data.setArrowTrail(null);
    }
}
