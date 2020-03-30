package net.skeagle.vrncore.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

@Getter
public class AFKManager {

    private static final Map<Player, AFKManager> afkPlayers = new HashMap<>();

    @Setter
    private boolean isAfk;
    private int timeAfk;
    private MouseLocation mouseLocation;

    public void setTimeAfk(final int timeAfk) {
        if (this.timeAfk <= -1) {
            return;
        }
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

    public void setMouseLocation(final Player p) {
        this.mouseLocation = new MouseLocation(p);
    }

    public void setMouseLocation(final MouseLocation loc) {
        this.mouseLocation = new MouseLocation(loc);
    }

    public boolean isLocEqual(final MouseLocation mouseLocation) {
        return getMouseLocation().isMouseLocationEqual(mouseLocation);
    }

    @Getter
    public static class MouseLocation {
        private final float yaw;
        private final float pitch;

        public MouseLocation(final Player player) {
            this.yaw = player.getLocation().getYaw();
            this.pitch = player.getLocation().getPitch();
        }

        public MouseLocation(final MouseLocation loc) {
            this.yaw = loc.getYaw();
            this.pitch = loc.getPitch();
        }

        boolean isMouseLocationEqual(final MouseLocation mouseLocation) {
            return mouseLocation.pitch == this.pitch && mouseLocation.yaw == this.yaw;
        }
    }
}
