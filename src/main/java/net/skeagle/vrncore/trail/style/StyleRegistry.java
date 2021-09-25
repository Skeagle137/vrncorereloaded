package net.skeagle.vrncore.trail.style;

import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrnlib.VRNLib;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StyleRegistry {

    private final Map<Style, TrailStyle> styles;
    private final Plugin plugin;

    public StyleRegistry(Plugin plugin) {
        styles = new HashMap<>();
        this.plugin = plugin;
        registerAll();
    }

    public void clear() {
        this.styles.clear();
    }

    public void registerAll() {
        List<Class<? extends TrailStyle>> list = VRNLib.getExtendingClasses(plugin, TrailStyle.class);
        for (Class<?> clazz : list) {
            try {
                TrailStyle style = (TrailStyle) clazz.getConstructor().newInstance();
                styles.put(style.getStyle(), style);
            } catch (ReflectiveOperationException e) {
                throw new IllegalStateException("Style " + clazz.getName() + " could not be registered", e);
            }
        }
    }

    public TrailStyle get(Style style) {
        return styles.get(style);
    }


}
