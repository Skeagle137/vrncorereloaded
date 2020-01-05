package net.skeagle.vrncore.utils;

import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Resource extends YamlConfiguration {
    private String name;
    private final File file;

    public Resource(final Plugin plugin, final String name) {
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
        }
        catch (IOException | InvalidConfigurationException ex2) {
            ex2.printStackTrace();
        }
    }

    public void save() {
        try {
            super.save(this.file);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getName() {
        return this.name;
    }

    public File getFile() {
        return this.file;
    }
}

