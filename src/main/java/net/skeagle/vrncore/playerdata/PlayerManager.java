package net.skeagle.vrncore.playerdata;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Particle;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerManager {

    private static final Map<UUID, PlayerData> playerMap = new HashMap<>();

    private static final PlayerManager instance = new PlayerManager();

    public static PlayerData getData(final UUID uuid) {
        PlayerData data = playerMap.get(uuid);
        if (data == null) {
            data = instance.load(uuid);
            playerMap.put(uuid, data);
        }
        return data;
    }

    private PlayerData load(final UUID uuid) {
        final SQLHelper db = VRNcore.getInstance().getDB();
        final SQLHelper.Results res = db.queryResults("SELECT * FROM playerdata WHERE id = (?)", uuid.toString());
        if (res.next())
            return new PlayerData(UUID.fromString(res.getString(1)), res.getString(2),
                    res.getString(3) != null ? Particle.valueOf(res.getString(3)) : null,
                    res.getString(4) != null ? Particle.valueOf(res.getString(4)) : null,
                    res.getBoolean(5),
                    res.getBoolean(6), res.getBoolean(7), res.getLong(8),
                    VRNUtil.LocationSerialization.deserialize(res.getString(9)), res.getLong(10));
        return new PlayerData(uuid, null, null, null,
                false, false, false, 0L, null, 0L);
    }

    public static void save() {
        playerMap.values().forEach(PlayerData::save);
    }
}
