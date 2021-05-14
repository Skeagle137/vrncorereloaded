package net.skeagle.vrncore.utils;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public final class AFKManager {

    private static final Map<Player, AFKManager> afkPlayers = new HashMap<>();

    private boolean afk;
    private int timeAfk;
    private SavedLocation savedLocation;

    public int getTimeAfk() {
        return timeAfk;
    }

    public void setTimeAfk(final int timeAfk) {
        if (timeAfk <= -1) return;
        this.timeAfk = timeAfk;
    }

    public static AFKManager getAfkManager(final Player p) {

        AFKManager manager = afkPlayers.get(p);

        if (manager == null) {
            manager = new AFKManager();

            afkPlayers.put(p, manager);
        }

        return manager;
    }

    public void setSavedLocation(final SavedLocation loc) {
        savedLocation = new SavedLocation(loc);
    }

    public boolean isYawEqual(final SavedLocation loc) {
        return getSavedLocation().isYawEqual(loc);
    }

    public boolean isPitchEqual(final SavedLocation loc) {
        return getSavedLocation().isPitchEqual(loc);
    }

    public void setAfk(final boolean afk) {
        this.afk = afk;
    }

    public boolean isAfk() {
        return this.afk;
    }

    public SavedLocation getSavedLocation() {
        return savedLocation;
    }

    public static class SavedLocation {
        private final float yaw;
        private final float pitch;

        public SavedLocation(final Player p) {
            this.yaw = p.getLocation().getYaw();
            this.pitch = p.getLocation().getPitch();
        }

        public SavedLocation(final SavedLocation loc) {
            this.yaw = loc.getYaw();
            this.pitch = loc.getPitch();
        }

        boolean isYawEqual(final SavedLocation loc) {
            return ((int) loc.yaw) == ((int) this.yaw);
        }

        boolean isPitchEqual(final SavedLocation loc) {
            return ((int) loc.pitch) == ((int) this.pitch);
        }

        public float getYaw() {
            return yaw;
        }

        public float getPitch() {
            return pitch;
        }
    }
}
