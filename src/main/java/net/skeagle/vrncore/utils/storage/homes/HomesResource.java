package net.skeagle.vrncore.utils.storage.homes;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HomesResource {

    @Getter
    private static final HomesResource instance = new HomesResource();

    @Getter
    private final List<HomesManager> manData = new ArrayList<>();

    public HomesManager getHome(final UUID uuid) {
        for (final HomesManager man : manData)
            if (man.getUUID().equals(uuid))
                return man;
        return new HomesManager(uuid);
    }

    public void loadAllHomes() {
        manData.clear();

        for (final File file : FileUtil.getFiles("homes", "yml")) {
            final HomesManager man = new HomesManager(UUID.fromString(file.getName().replace(".yml", "")));
            manData.add(man);
        }
    }
}
