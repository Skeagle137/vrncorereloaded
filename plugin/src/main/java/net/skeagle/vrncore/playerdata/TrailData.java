package net.skeagle.vrncore.playerdata;

import com.google.gson.JsonObject;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailColors;
import net.skeagle.vrncore.trail.style.TrailStyle;
import net.skeagle.vrncore.event.TrailDataUpdateEvent;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.style.StyleRegistry;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

public class TrailData {

    private final StyleRegistry registry;
    private final TrailType type;
    private Particle particle;
    private Color color;
    private Color fade;
    private double size;
    private TrailStyle style;
    private double note = TrailColors.RED.getNote();

    public TrailData(TrailType type) {
        this.registry = VRNCore.getInstance().getStyleRegistry();
        this.type = type;
        this.color = Color.RED;
        this.fade = Color.WHITE;
        this.size = 1.0;
        this.style = registry.get(Style.DEFAULT);
    }

    public TrailData(VRNCore plugin, TrailType type, Particle particle, Color color, Color fade, double size, TrailStyle style) {
        this.registry = plugin.getStyleRegistry();
        this.type = type;
        this.particle = particle;
        this.color = color;
        this.fade = fade;
        this.size = size;
        this.style = style;
    }

    public TrailType getType() {
        return type;
    }

    public Particle getParticle() {
        return particle;
    }

    public void setParticle(Player player, Particle particle) {
        this.particle = particle;
        Bukkit.getPluginManager().callEvent(new TrailDataUpdateEvent(player, this));
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Player player, Color color) {
        this.color = color;
        TrailColors trailColor = TrailColors.getFromColor(color);
        if (trailColor != null) {
            this.note = trailColor.getNote();
        }
        Bukkit.getPluginManager().callEvent(new TrailDataUpdateEvent(player, this));
    }

    public Color getFade() {
        return fade;
    }

    public void setFade(Player player, Color fade) {
        this.fade = fade;
        Bukkit.getPluginManager().callEvent(new TrailDataUpdateEvent(player, this));
    }

    public double getSize() {
        return size;
    }

    public void setSize(Player player, double size) {
        this.size = size;
        Bukkit.getPluginManager().callEvent(new TrailDataUpdateEvent(player, this));
    }

    public TrailStyle getStyle() {
        return style;
    }

    public void setStyle(Player player, Style style) {
        this.style = registry.get(style);
        Bukkit.getPluginManager().callEvent(new TrailDataUpdateEvent(player, this));
    }

    public double getNote() {
        return note;
    }

    JsonObject serialize() {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("particle", particle == null ? null : particle.toString());
        jsonObject.addProperty("color", color.asRGB());
        jsonObject.addProperty("fade", fade.asRGB());
        jsonObject.addProperty("size", size);
        jsonObject.addProperty("style", VRNCore.getInstance().getStyleRegistry().getStyle(style).toString());
        return jsonObject;
    }

    static TrailData deserialize(VRNCore plugin, TrailType type, JsonObject jsonObject) {
        try {
            return new TrailData(plugin, type, Particle.valueOf(jsonObject.get("particle").getAsString()),
                    Color.fromRGB(jsonObject.get("color").getAsInt()), Color.fromRGB(jsonObject.get("fade").getAsInt()),
                    jsonObject.get("size").getAsDouble(), plugin.getStyleRegistry().get(Style.valueOf(jsonObject.get("style").getAsString())));
        }
        catch (Exception ex) {
            return new TrailData(type);
        }
    }
}
