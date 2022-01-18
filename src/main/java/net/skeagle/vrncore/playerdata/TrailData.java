package net.skeagle.vrncore.playerdata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailColors;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.trail.style.StyleRegistry;
import net.skeagle.vrncore.trail.style.TrailStyle;
import org.bukkit.Color;
import org.bukkit.Particle;

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
        this.registry = VRNcore.getInstance().getStyleRegistry();
        this.type = type;
        this.color = Color.RED;
        this.fade = Color.WHITE;
        this.size = 1.0;
        this.style = registry.get(Style.DEFAULT);
    }

    public TrailData(VRNcore plugin, TrailType type, Particle particle, Color color, Color fade, double size, TrailStyle style) {
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

    public void setParticle(Particle particle) {
        this.particle = particle;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
        TrailColors trailColor = TrailColors.getFromColor(color);
        if (trailColor != null) {
            this.note = trailColor.getNote();
        }
    }

    public Color getFade() {
        return fade;
    }

    public void setFade(Color fade) {
        this.fade = fade;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public TrailStyle getStyle() {
        return style;
    }

    public void setStyle(Style style) {
        this.style = registry.get(style);
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
        jsonObject.addProperty("style", VRNcore.getInstance().getStyleRegistry().getStyle(style).toString());
        return jsonObject;
    }

    static TrailData deserialize(VRNcore plugin, TrailType type, JsonObject jsonObject) {
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
