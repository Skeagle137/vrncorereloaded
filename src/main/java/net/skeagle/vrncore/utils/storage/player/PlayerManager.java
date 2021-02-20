package net.skeagle.vrncore.utils.storage.player;

import lombok.Getter;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.api.DBObject;
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

    public static PlayerData getData(final Player p) {

        PlayerData data = playerMap.get(p.getUniqueId());

        if (data == null) {
            data = instance.loadData(p);

            playerMap.put(p.getUniqueId(), data);
        }

        return data;
    }

    private PlayerData loadData(Player p) {
        try {
            PreparedStatement ps = getConn().prepareStatement("SELECT * FROM " + getName() + " WHERE uuid = ?");
            ps.setString(1, p.getUniqueId().toString());
            try (final ResultSet rs = ps.executeQuery()) {
                Location loc = VRNUtil.LocationSerialization.deserialize(rs.getString("last_location"));
                if (loc == null) loc = p.getLocation();
                return new PlayerData(p.getUniqueId(), rs.getString("nickname"),
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
                ps.setString(1, p.getUniqueId().toString());
                ps.setNull(2, Types.NULL);
                ps.setNull(3, Types.NULL);
                ps.setNull(4, Types.NULL);
                ps.setBoolean(5, false);
                ps.setBoolean(6, false);
                ps.setBoolean(7, false);
                ps.setLong(8, 0);
                ps.setString(9, VRNUtil.LocationSerialization.serialize(p.getLocation()));
                ps.setLong(10, 0);
                ps.execute();
                loadData(p);
            }
            catch (Exception e2) {
                Common.log("Could not create player data");
                e2.printStackTrace();
            }
        }
        return null;
    }
}
