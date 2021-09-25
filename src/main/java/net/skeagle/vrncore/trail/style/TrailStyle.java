package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.trail.Particles;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.TrailVisibility;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public abstract class TrailStyle {

    private final Style style;

    public TrailStyle(Style style) {
        this.style = style;
    }

    public abstract void tick(Player player, Particles particle, Location loc, TrailType type, TrailVisibility visibility);

    Style getStyle() {
        return style;
    }
}