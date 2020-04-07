package net.skeagle.vrncore.utils.warps;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class WarpsResource {

    @Getter
    private static final WarpsResource instance = new WarpsResource();

    @Getter
    private final List<WarpsManager> manData = new ArrayList<>();

    public boolean setWarp(final Player p, final String name) {
        for (final WarpsManager man : manData)
            if (man.getName().equalsIgnoreCase(name))
                return false;

        final WarpsManager man = new WarpsManager(name);
        man.setLoc(p.getLocation());
        man.save();
        manData.add(man);
        return true;
    }

    public boolean delWarp(final String name) {
        for (final WarpsManager man : manData)
            if (man.getName().equalsIgnoreCase(name)) {
                man.delete();
                manData.remove(man);
                return true;
            }
        return false;
    }

    public WarpsManager getWarp(final String name) {
        for (final WarpsManager man : manData)
            if (man.getName().equalsIgnoreCase(name))
                return man;

        return null;
    }

    public List<String> getWarpNames() {
        final List<String> names = new ArrayList<>();
        for (final WarpsManager man : manData) {
            names.add(man.getName());
        }
        return names;
    }

    public void loadAllWarps() {
        manData.clear();

        for (final File file : FileUtil.getFiles("warps", "yml")) {
            final WarpsManager man = new WarpsManager(file.getName().replace(".yml", ""));
            manData.add(man);
        }
    }
}