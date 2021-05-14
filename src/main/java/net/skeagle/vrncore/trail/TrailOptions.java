package net.skeagle.vrncore.trail;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;


public final class TrailOptions {

    private final ChatColor color;
    private final TrailStyle style;

    protected TrailOptions(final ChatColor color, final TrailStyle style) {
        this.color = color;
        this.style = style;
    }

    public String serialize(final TrailOptions options, final Player p) {
        return options.getColor().getChar() + "\"" + options.getStyle().name();
    }

    public static TrailOptions deserialize(final String s) {
        final String[] split = s.replaceAll("\"", "").split(" ");
        if (split.length != 2) {
            return null;
        }
        return new TrailOptions(ChatColor.getByChar(split[0]), TrailStyle.valueOf(split[1]));
    }

    public void resetOptions(final Player p) {

    }

    public ChatColor getColor() {
        return color;
    }

    public TrailStyle getStyle() {
        return style;
    }
}
