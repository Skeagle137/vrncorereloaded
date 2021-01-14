package net.skeagle.vrncore.utils.storage.player;

import lombok.Getter;
import net.skeagle.vrncore.db.DBConnect;
import net.skeagle.vrncore.utils.storage.api.SkipPrimaryID;
import net.skeagle.vrncore.utils.storage.api.StoreableObject;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.mineacademy.fo.Common;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

@SkipPrimaryID @Getter
public class PlayerData extends StoreableObject<PlayerData> {

    private final UUID uuid;
    private String nickname;
    private Particle arrowtrail;
    private Particle playertrail;
    private Boolean vanished;
    private Boolean muted;
    private Boolean godmode;
    private Long last_online;
    private Location last_location;
    private Long timeplayed;

    PlayerData(UUID uuid, String nickname, Particle arrowtrail, Particle playertrail,
               Boolean vanished, Boolean muted, Boolean godmode, Long last_online,
               Location last_location, Long timeplayed) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.arrowtrail = arrowtrail;
        this.playertrail = playertrail;
        this.vanished = vanished;
        this.muted = muted;
        this.godmode = godmode;
        this.last_online = last_online;
        this.last_location = last_location;
        this.timeplayed = timeplayed;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
        update("nickname", nickname);
    }

    public void setArrowtrail(@Nullable Particle arrowtrail) {
        this.arrowtrail = arrowtrail;
        update("arrowtrail", arrowtrail);
    }

    public void setPlayertrail(@Nullable Particle playertrail) {
        this.playertrail = playertrail;
        update("playertrail", playertrail);
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
        update("vanished", vanished);
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
        update("muted", muted);
    }

    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
        update("godmode", godmode);
    }

    public void setLastOnline(long last_online) {
        this.last_online = last_online;
        update("last_online", last_online);
    }

    public void setLastLocation(Location last_location) {
        this.last_location = last_location;
        update("last_location", last_location);
    }

    public void setTimeplayed(Long timeplayed) {
        this.timeplayed = timeplayed;
        update("timeplayed", timeplayed);
    }

    private void update(String s, Object o) {
        try {
            PreparedStatement ps = DBConnect.getConn().prepareStatement("UPDATE playerdata SET " + s + " = ? WHERE uuid = ?");
            if (o == null)
                ps.setNull(1, Types.NULL);
            else {
                if (o instanceof Boolean)
                    ps.setBoolean(1, (boolean) o);
                else if (o instanceof Long)
                    ps.setLong(1, (long) o);
                else if (o instanceof Particle)
                    ps.setString(1, ((Particle) o).name());
                else
                    ps.setString(1, (String) o);
                ps.setString(2, uuid.toString());
            }
            ps.executeUpdate();
        }
        catch (SQLException e) {
            Common.log("Could not save player data.");
            e.printStackTrace();
        }
    }
}
