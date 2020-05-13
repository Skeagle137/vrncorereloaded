package net.skeagle.vrncore.utils;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

@Getter
@Setter
public class TrailOptions {

    private ChatColor color;
    private TrailStyle style;

    protected TrailOptions(ChatColor color, TrailStyle style) {
        this.color = color;
        this.style = style;
    }

    public String serialize(final TrailOptions options, final Player p) {
        return options.getColor().getChar() + " " + options.getStyle().name();
    }

    public static TrailOptions deserialize(final String s) {
        final String[] split = s.replaceAll("\"", "").split(" ");
        if (split.length != 6) {
            return null;
        }
        return new TrailOptions(ChatColor.getByChar(split[0]), TrailStyle.valueOf(split[1]));
    }

    public void resetOptions(final Player p) {

    }


}
