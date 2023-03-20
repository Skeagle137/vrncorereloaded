package net.skeagle.vrncore.trail;

import net.skeagle.vrncommands.misc.FormatUtils;

public enum TrailType {
    PLAYER,
    ARROW;

    public String getName() {
        return FormatUtils.toTitleCase(this.name());
    }
}
