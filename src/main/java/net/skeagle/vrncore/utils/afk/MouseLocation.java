package net.skeagle.vrncore.utils.afk;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class MouseLocation {
    @Getter
    private float yaw;

    @Getter
    private float pitch;

    public MouseLocation(final Location location) {
        this.yaw = location.getYaw();
        this.pitch = location.getPitch();
    }

    public MouseLocation(final float yaw, final float pitch) {
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public MouseLocation(final Player player) {
        this.yaw = player.getLocation().getYaw();
        this.pitch = player.getLocation().getPitch();
    }

    public boolean isMouseLocationEqual(final MouseLocation mouseLocation) {
        return mouseLocation.pitch == this.pitch && mouseLocation.yaw == this.yaw;
    }
}
