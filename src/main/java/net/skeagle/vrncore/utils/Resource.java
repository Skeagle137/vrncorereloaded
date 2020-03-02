package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Resource extends YamlConfiguration {
    private final String name;
    private final File file;

    public Resource(final VRNcore plugin, final String name) {
        this.name = name;
        this.file = new File(plugin.getDataFolder(), name);
        if (!this.file.getParentFile().exists()) {
            this.file.getParentFile().mkdirs();
        }
        if (!this.file.exists()) {
            plugin.saveResource(name, true);
        }
    }

    public void load() {
        try {
            super.load(this.file);
        } catch (final IOException | InvalidConfigurationException ex2) {
            ex2.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.file);
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }
}

