package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.Settings;
import net.skeagle.vrnlib.misc.EventListener;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class AFKManager {

    private static final Map<UUID, AFKManager> afkPlayers = new HashMap<>();

    private boolean afk;
    private int timeAfk;
    private Location savedLocation;
    private int idle = Settings.idleTrailActivation;

    public AFKManager() {
        new EventListener<>(PlayerMoveEvent.class, e -> {
            AFKManager manager = getAfkManager(e.getPlayer());
            Location loc = e.getPlayer().getLocation();
            if (manager.savedLocation == null) return;
            if (manager.savedLocation.getX() != loc.getX() || manager.savedLocation.getY() != loc.getY() || manager.savedLocation.getZ() != loc.getZ()) {
                manager.idle = Settings.idleTrailActivation;
            }
        });
    }

    public static AFKManager getAfkManager(final Player p) {
        AFKManager manager = afkPlayers.get(p.getUniqueId());
        if (manager == null) {
            manager = new AFKManager();
            afkPlayers.put(p.getUniqueId(), manager);
        }
        return manager;
    }

    public int getTimeAfk() {
        return timeAfk;
    }

    public void setTimeAfk(final int timeAfk) {
        this.timeAfk = timeAfk <= -1 ? 0 : timeAfk;
    }

    public void decrementIdleCountdown() {
        this.idle--;
    }

    public boolean isIdle() {
        return this.idle < 0;
    }

    public void remove(Player player) {
        afkPlayers.remove(player.getUniqueId());
    }

    public void setSavedLocation(final Location loc) {
        savedLocation = loc;
    }

    public void setAfk(final boolean afk) {
        this.afk = afk;
    }

    public boolean isAfk() {
        return this.afk;
    }

    public Location getSavedLocation() {
        return savedLocation;
    }
}
