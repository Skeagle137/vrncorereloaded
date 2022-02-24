package net.skeagle.vrncore.playerdata;

import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.trail.Style;
import net.skeagle.vrncore.trail.TrailType;
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
        VRNcore plugin = VRNcore.getInstance();
        final SQLHelper db = plugin.getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM playerdata WHERE id = (?)", uuid.toString());
        if (!res.isEmpty()) {
            final JsonObject playerTrailData = VRNUtil.GSON.fromJson(res.getString(3), JsonObject.class);
            final JsonObject arrowTrailData = VRNUtil.GSON.fromJson(res.getString(4), JsonObject.class);
            final PlayerStates states = VRNUtil.GSON.fromJson(res.getString(5), PlayerStates.class);
            return new PlayerData(UUID.fromString(res.getString(1)), res.getString(2),
                    playerTrailData != null ? TrailData.deserialize(plugin, TrailType.PLAYER, playerTrailData) : new TrailData(TrailType.PLAYER),
                    arrowTrailData != null ? TrailData.deserialize(plugin, TrailType.ARROW, arrowTrailData) : new TrailData(TrailType.ARROW),
                    states != null ? states : new PlayerStates(), res.getLong(6));
        }
        return new PlayerData(uuid);
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
