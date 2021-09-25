package net.skeagle.vrncore.playerdata;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.style.StyleRegistry;
import net.skeagle.vrncore.trail.style.TrailStyle;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.UserCache;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.*;

public class PlayerManager {

    private static final Map<UUID, PlayerData> playerMap = new HashMap<>();

    public static PlayerData getData(final UUID uuid) {
        PlayerData data = playerMap.get(uuid);
        if (data == null) {
            data = load(uuid);
            playerMap.put(uuid, data);
        }
        return data;
    }

    private static PlayerData load(final UUID uuid) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM playerdata WHERE id = (?)", uuid.toString());
        if (!res.isEmpty()) {
            return new PlayerData(UUID.fromString(res.getString(1)), res.getString(2),
                    res.getString(3) != null ? Particle.valueOf(res.getString(3)) : null,
                    res.getString(4) != null ? Particle.valueOf(res.getString(4)) : null,
                    VRNcore.getInstance().getStyleRegistry().get(res.getString(5) != null ? Style.valueOf(res.getString(5)) : Style.DEFAULT),
                    res.getBoolean(6), res.getBoolean(7), res.getBoolean(8),
                    VRNUtil.LocationSerialization.deserialize(res.getString(9)), res.getLong(10));
        }
        return new PlayerData(uuid, null, null, null, VRNcore.getInstance().getStyleRegistry().get(Style.DEFAULT),
                false, false, false, null, 0L);
    }

    public OfflinePlayer getOfflinePlayer(final String name) {
        final Player player = Bukkit.getPlayer(name);
        if (player != null)
            return player;
        final OfflinePlayer offPlayer = UserCache.getOfflinePlayer(name);
        if (offPlayer != null)
            return offPlayer;
        final String s = VRNcore.getInstance().getDB().querySingleResult("SELECT id FROM playerdata WHERE name = (?)", name.toLowerCase(Locale.ROOT));
        return s != null ? Bukkit.getOfflinePlayer(UUID.fromString(s)) : null;
    }

    public void save() {
        playerMap.values().forEach(PlayerData::save);
    }
}
