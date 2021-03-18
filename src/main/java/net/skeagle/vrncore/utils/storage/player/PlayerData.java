package net.skeagle.vrncore.utils.storage.player;

import net.skeagle.vrncore.api.sql.SQLConnection;
import net.skeagle.vrncore.api.sql.SkipPrimaryID;
import net.skeagle.vrncore.api.sql.StoreableObject;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.mineacademy.fo.Common;

import javax.annotation.Nullable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Types;
import java.util.UUID;

@SkipPrimaryID
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

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public Particle getArrowtrail() {
        return arrowtrail;
    }

    public void setArrowtrail(@Nullable Particle arrowtrail) {
        this.arrowtrail = arrowtrail;
    }

    public Particle getPlayertrail() {
        return playertrail;
    }

    public void setPlayertrail(@Nullable Particle playertrail) {
        this.playertrail = playertrail;
    }

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(boolean vanished) {
        this.vanished = vanished;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(boolean muted) {
        this.muted = muted;
    }

    public boolean isGodmode() {
        return godmode;
    }

    public void setGodmode(boolean godmode) {
        this.godmode = godmode;
    }

    public long getLastOnline() {
        return last_online;
    }

    public void setLastOnline(Long last_online) {
        this.last_online = last_online;
    }

    public Location getLastLocation() {
        return last_location;
    }

    public void setLastLocation(Location last_location) {
        this.last_location = last_location;
    }

    public long getTimeplayed() {
        return timeplayed;
    }

    public void setTimeplayed(Long timeplayed) {
        this.timeplayed = timeplayed;
    }

    private void update(String s, Object o) {
        try {
            PreparedStatement ps = SQLConnection.getConnection().prepareStatement("UPDATE playerdata SET " + s + " = ? WHERE uuid = ?");
            if (o == null)
                ps.setNull(1, Types.NULL);
            else {
                if (o instanceof Boolean)
                    ps.setBoolean(1, (Boolean) o);
                else if (o instanceof Long)
                    ps.setLong(1, (Long) o);
                else if (o instanceof Particle)
                    ps.setString(1, ((Particle) o).name());
                else if (o instanceof Location)
                    ps.setString(1, VRNUtil.LocationSerialization.serialize((Location) o));
                else
                    ps.setString(1, (String) o);
            }
            ps.setString(2, uuid.toString());
            ps.executeUpdate();
        } catch (SQLException e) {
            Common.log("Could not save player data.");
            e.printStackTrace();
        }
    }

    public void save() {
        update("nickname", nickname);
        update("arrowtrail", arrowtrail);
        update("playertrail", playertrail);
        update("vanished", vanished);
        update("muted", muted);
        update("godmode", godmode);
        update("last_online", last_online);
        update("last_location", last_location);
        update("timeplayed", timeplayed);
    }
}
