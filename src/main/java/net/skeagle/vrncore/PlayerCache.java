package net.skeagle.vrncore;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerCache extends YamlSectionConfig {

    private static Map<UUID, PlayerCache> cacheMap = new HashMap<>();

    private String nickname;
    private CompParticle arrowtrail;
    private CompParticle playertrail;
    private boolean vanished;
    private boolean muted;
    private boolean godmode;

    protected PlayerCache(final String uuid) {
        super(uuid);
        loadConfiguration(null, "data.db");
    }

    @Override
    protected void onLoadFinish() {
        if (isSet("Nickname")) {
            nickname = getString("Nickname");
        }

        if (isSet("Arrowtrail"))
            arrowtrail = get("Arrowtrail", CompParticle.class);

        if (isSet("PlayerTrail"))
            playertrail = get("Playertrail", CompParticle.class);

        if (isSet("Vanish"))
            vanished = getBoolean("Vanished");

        if (isSet("Muted"))
            muted = getBoolean("Muted");

        if (isSet("Godmode"))
            godmode = getBoolean("Godmode");
    }

    public void setNickname(final String nickname) {
        this.nickname = nickname;

        save("Nickname", nickname);
    }

    public void setArrowtrail(final CompParticle arrowtrail) {
        this.arrowtrail = arrowtrail;

        save("Arrowtrail", arrowtrail);
    }

    public void setPlayertrail(final CompParticle playertrail) {
        this.playertrail = playertrail;

        save("Playertrail", playertrail);
    }

    public void setVanished(final Boolean vanished) {
        this.vanished = vanished;

        save("Vanished", vanished);
    }

    public void setMuted(final Boolean muted) {
        this.muted = muted;

        save("Muted", muted);
    }

    public void setGodmode(final Boolean godmode) {
        this.godmode = godmode;

        save("Godmode", godmode);
    }

    public static PlayerCache getCache(final Player p) {

        PlayerCache cache = cacheMap.get(p.getUniqueId());

        if (cache == null) {
            cache = new PlayerCache(p.getUniqueId().toString());

            cacheMap.put(p.getUniqueId(), cache);
        }

        return cache;
    }
}
