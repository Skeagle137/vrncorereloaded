package net.skeagle.vrncore.utils.storage.npc;

import net.skeagle.vrncore.api.util.SkinUtil;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Location;
import org.mineacademy.fo.settings.YamlConfig;

public class NPCManager extends YamlConfig {

    private String name;
    private String display;
    private Location loc;
    private SkinUtil skin;
    private boolean rotatehead;

    NPCManager(final String name) {
        setHeader("");
        loadConfiguration(null, "npcs/" + name + (!name.toLowerCase().endsWith(".yml") ? ".yml" : ""));
    }

    @Override
    protected void onLoadFinish() {
        if (isSet("Name")) {
            name = getString("Name");
        }
        if (isSet("Display_Name")) {
            display = getString("Display_Name");
        }
        if (isSet("Location")) {
            loc = VRNUtil.LocationSerialization.deserialize(getString("Location"));
        }
        if (isSet("Skin")) {
            skin = SkinUtil.deserialize(getString("Skin"));
        }
        if (isSet("Rotate_head")) {
            rotatehead = getBoolean("Rotate_head");
        }
    }

    public void setName(final String name) {
        this.name = name;

        save("Name", name);
    }

    public void setDisplay(final String name) {
        this.display = VRNUtil.color(name);

        save("Display_Name", name);
    }

    public void setLoc(final Location loc) {
        this.loc = loc;

        save("Location", VRNUtil.LocationSerialization.serialize(loc));
    }

    public void setSkin(final String name) {
        this.skin = new SkinUtil(name);

        save("Skin", skin.serialize());
    }

    public void setRotatehead(final Boolean rotatehead) {
        this.rotatehead = rotatehead;

        save("Rotate_head", rotatehead);
    }
    
    public String getName() {
        return name;
    }

    public Location getLoc() {
        return loc;
    }

    public String getDisplay() {
        return display;
    }

    public SkinUtil getSkin() {
        return skin;
    }

    public String getNPCName() {
        return getFileName().replace(".yml", "");
    }
}
