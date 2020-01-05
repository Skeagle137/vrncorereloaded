package net.skeagle.vrncore.utils;


import net.skeagle.vrncore.VRNcore;

public class Resources
{
    private VRNcore plugin;
    private Resource warps;
    private Resource playerdata;

    public Resources(final VRNcore plugin) {
        this.plugin = plugin;
        this.warps = new Resource(plugin, "warps.yml");
        this.playerdata = new Resource(plugin, "playerdata.yml");
    }

    public void load() {
        warps.load();
        playerdata.load();
    }

    public void reload() {
        this.load();
    }

    public void save() {
        warps.save();
        playerdata.save();
    }

    public Resource getWarps() {
        return warps;
    }

    public Resource getplayerData() {
        return playerdata;
    }
}

