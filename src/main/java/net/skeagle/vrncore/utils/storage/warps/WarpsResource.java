package net.skeagle.vrncore.utils.storage.warps;

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

    public void loadAllWarps() {
        manData.clear();

        for (final File file : FileUtil.getFiles("warps", "yml")) {
            final WarpsManager man = new WarpsManager(file.getName().replace(".yml", ""));
            manData.add(man);
        }
    }
}