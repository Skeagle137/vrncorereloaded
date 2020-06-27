package net.skeagle.vrncore;

import lombok.Getter;
import net.skeagle.vrncore.utils.TrailOptions;
import org.bukkit.entity.Player;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.settings.YamlSectionConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PlayerCache extends YamlSectionConfig {

    private static final Map<UUID, PlayerCache> cacheMap = new HashMap<>();

    private String nickname;
    private CompParticle arrowtrail;
    private CompParticle playertrail;
    private TrailOptions options;
    private boolean vanished;
    private boolean muted;
    private boolean godmode;
    private int timeplayed;

    protected PlayerCache(final String uuid) {
        super(uuid);
        setHeader("This is the main settings file.\n\n");
        loadConfiguration(null, "data.db");
    }

    @Override
    protected void onLoadFinish() {
        if (isSet("Nickname")) {
            nickname = getString("Nickname");
        }

        if (isSet("Arrowtrail"))
            arrowtrail = get("Arrowtrail", CompParticle.class);

        if (isSet("Playertrail"))
            playertrail = get("Playertrail", CompParticle.class);

        if (isSet("Vanish"))
            vanished = getBoolean("Vanished");

        if (isSet("Muted"))
            muted = getBoolean("Muted");

        if (isSet("Godmode"))
            godmode = getBoolean("Godmode");

        if (isSet("Timeplayed"))
            timeplayed = getInteger("Timeplayed");
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

    public void setTimeplayed(final Integer timeplayed) {
        this.timeplayed = timeplayed;

        save("Timeplayed", timeplayed);
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
