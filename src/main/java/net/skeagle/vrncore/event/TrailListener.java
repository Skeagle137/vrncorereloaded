package net.skeagle.vrncore.event;

import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.SuperVanishHook;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.playerdata.TrailData;
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
                final Particles trail = Particles.getFromParticle(data.getPlayerTrailData().getParticle());
                if (trail == null) {
                    continue;
                }
                if (pl.hasPermission(trail.getPermission(TrailType.PLAYER))) {
                    data.getPlayerTrailData().getStyle().tick(pl, pl.getLocation(), data.getPlayerTrailData(), trail,
                            HookManager.isSuperVanishLoaded() && SuperVanishHook.isVanished(pl) ? TrailVisibility.CLIENT : TrailVisibility.ALL);
                    continue;
                }
                data.getPlayerTrailData().setParticle(null);
            }
            tick = ++tick % 60;
        }, 20L, 1L);
    }

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player player) || !(e.getEntity() instanceof Arrow arrow)) return;
        final TrailData data = PlayerManager.getData(player.getUniqueId()).getArrowTrailData();
        final Particles trail = Particles.getFromParticle(data.getParticle());
        if (trail == null) {
            return;
        }
        if (player.hasPermission(trail.getPermission(TrailType.ARROW))) {
            Task.syncRepeating(run -> {
                if (!arrow.isValid() || arrow.isOnGround()) {
                    run.cancel();
                    return;
                }
                data.getStyle().tick(player, arrow.getLocation(), data, trail, TrailVisibility.ALL);
            }, 2, 1);
            return;
        }
        data.setParticle(null);
    }
}
