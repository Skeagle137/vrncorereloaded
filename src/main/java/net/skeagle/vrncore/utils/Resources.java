package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.VRNcore;

public class Resources
{
    private final Resource warps;

    public Resources(final VRNcore plugin) {

        this.warps = new Resource(plugin, "warps.yml");
    }

    public void load() {
        warps.load();
    }

    public void save() {
        warps.save();
    }

    public Resource getWarps() {
        return warps;
    }
}

