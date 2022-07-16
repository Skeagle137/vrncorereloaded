package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.SuperVanishHook;
import net.skeagle.vrncore.playerdata.TrailData;
import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TrailHandler implements Listener {

    private Task trailTask;
    public static int tick = 0;
    private final Map<Player, WeakReference<TrailData>> trailCache;
    private final Map<Player, WeakReference<TrailData>> arrowCache;

    public TrailHandler(VRNcore plugin) {
        trailCache = Collections.synchronizedMap(new ConcurrentHashMap<>());
        arrowCache = Collections.synchronizedMap(new ConcurrentHashMap<>());
        this.startParticleTask();
        new EventListener<>(PlayerJoinEvent.class, e -> {
            plugin.getPlayerManager().getData(e.getPlayer().getUniqueId()).thenAccept(data -> {
                if (data.getPlayerTrailData() != null) {
                    trailCache.put(e.getPlayer(), new WeakReference<>(data.getPlayerTrailData()));
                }
                if (data.getArrowTrailData() != null) {
                    arrowCache.put(e.getPlayer(), new WeakReference<>(data.getArrowTrailData()));
                }
            });
        });
        new EventListener<>(PlayerQuitEvent.class, e -> {
            trailCache.remove(e.getPlayer());
            arrowCache.remove(e.getPlayer());
        });
        new EventListener<>(TrailDataUpdateEvent.class, e -> {
            if (e.getTrailData().getType() == TrailType.PLAYER) {
                if (e.getTrailData().getParticle() == null) {
                    trailCache.remove(e.getPlayer());
                    return;
                }
                trailCache.put(e.getPlayer(), new WeakReference<>(e.getTrailData()));
                return;
            }
            if (e.getTrailData().getParticle() == null) {
                arrowCache.remove(e.getPlayer());
                return;
            }
            arrowCache.put(e.getPlayer(), new WeakReference<>(e.getTrailData()));
        });
    }

    public void startParticleTask() {
        if (trailTask != null) {
            trailTask.cancel();
        }

        trailTask = Task.syncRepeating(() -> {
            trailCache.keySet().forEach(this::handleTrail);
            tick = ++tick % 60;
        }, 20L, 1L);
    }

    @EventHandler
    public void onArrowShot(final ProjectileLaunchEvent e) {
        if (!(e.getEntity().getShooter() instanceof Player player) || !(e.getEntity() instanceof Arrow arrow)) return;
        if (arrowCache.containsKey(player)) {
            this.handleArrow(player, arrow);
        }
    }

    private void handleTrail(Player player) {
        TrailData data = trailCache.get(player).get();
        if (data == null) {
            trailCache.remove(player);
        }
        final Particles trail = Particles.getFromParticle(data.getParticle());
        if (trail == null) {
            data.setParticle(player, null);
            return;
        }
        if (player.hasPermission(trail.getPermission(TrailType.PLAYER))) {
            data.getStyle().tick(player, player.getLocation(), data, trail,
                    HookManager.isSuperVanishLoaded() && SuperVanishHook.isVanished(player) ? TrailVisibility.CLIENT : TrailVisibility.ALL);
            return;
        }
        data.setParticle(player, null);
    }

    private void handleArrow(Player player, Arrow arrow) {
        TrailData data = arrowCache.get(player).get();
        if (data == null) {
            arrowCache.remove(player);
        }
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
            }, 1, 1);
            return;
        }
        data.setParticle(player, null);
    }
}
