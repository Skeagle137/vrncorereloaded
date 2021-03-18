package net.skeagle.vrncore.utils.storage.player;

import lombok.Getter;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.api.sql.DBObject;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.*;

@Getter
public class PlayerManager extends DBObject<PlayerData> {

    public PlayerManager() {
        super("playerdata", PlayerData.class);
    }

    private static final Map<UUID, PlayerData> playerMap = new HashMap<>();

    private static final PlayerManager instance = new PlayerManager();

    public static PlayerData getData(final UUID uuid) {
        PlayerData data = playerMap.get(uuid);
        if (data == null) {
            data = instance.loadData(uuid);
            playerMap.put(uuid, data);
        }
        return data;
    }

    private PlayerData loadData(UUID uuid) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE uuid = ?");
            ps.setString(1, uuid.toString());
            try (final ResultSet rs = ps.executeQuery()) {
                Location loc = VRNUtil.LocationSerialization.deserialize(rs.getString("last_location"));
                return new PlayerData(uuid, rs.getString("nickname"),
                        rs.getString("arrowtrail") != null ? Particle.valueOf(rs.getString("arrowtrail")) : null,
                        rs.getString("playertrail") != null ? Particle.valueOf(rs.getString("playertrail")) : null,
                        rs.getBoolean("vanished"),
                        rs.getBoolean("muted"), rs.getBoolean("godmode"), rs.getLong("last_online"),
                        loc, rs.getLong("timeplayed"));
            }
        }
        catch (SQLException e) {
            try {
                PreparedStatement ps = getConn().prepareStatement("INSERT INTO " + getName() +
                        " (uuid, nickname, arrowtrail, playertrail, vanished, muted, godmode, last_online, last_location, timeplayed) " +
                        "VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
                ps.setString(1, uuid.toString());
                ps.setNull(2, Types.NULL);
                ps.setNull(3, Types.NULL);
                ps.setNull(4, Types.NULL);
                ps.setBoolean(5, false);
                ps.setBoolean(6, false);
                ps.setBoolean(7, false);
                ps.setLong(8, 0);
                ps.setNull(9, Types.NULL);
                ps.setLong(10, 0);
                ps.execute();
                loadData(uuid);
            }
            catch (Exception e2) {
                Common.log("Could not create player data");
                e2.printStackTrace();
            }
        }
        return null;
    }
}
