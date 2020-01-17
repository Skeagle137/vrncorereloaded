package net.skeagle.vrncore;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompParticle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter @Setter
public class PlayerCache {

    private static Map<UUID, PlayerCache> cacheMap = new HashMap<>();

    private String nickname;
    private CompParticle arrowtrail;
    private boolean vanished;
    private boolean muted;
    private boolean godmode;

    public static PlayerCache getCache(final Player p) {

        PlayerCache cache = cacheMap.get(p.getUniqueId());

        if (cache == null) {
            cache = new PlayerCache();

            cacheMap.put(p.getUniqueId(), cache);
        }

        return cache;
    }
}
