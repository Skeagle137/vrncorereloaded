package net.skeagle.vrncore.playerdata;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Location;
import org.bukkit.Particle;

import javax.annotation.Nullable;
import java.util.UUID;

public class PlayerData {

    private final UUID uuid;
    private String nickname;
    private Particle arrowtrail;
    private Particle playertrail;
    private boolean vanished;
    private boolean muted;
    private boolean godmode;
    private long last_online;
    private Location last_location;
    private long timeplayed;

    PlayerData(final UUID uuid, final String nickname, final Particle arrowtrail, final Particle playertrail,
               final boolean vanished, final boolean muted, final boolean godmode, final long last_online,
               final Location last_location, final long timeplayed) {
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

    public void setNickname(final String nickname) {
        this.nickname = nickname;
    }

    public Particle getArrowtrail() {
        return arrowtrail;
    }

    public void setArrowtrail(@Nullable final Particle arrowtrail) {
        this.arrowtrail = arrowtrail;
    }

    public Particle getPlayertrail() {
        return playertrail;
    }

    public void setPlayertrail(@Nullable final Particle playertrail) {
        this.playertrail = playertrail;
    }

    public boolean isVanished() {
        return vanished;
    }

    public void setVanished(final boolean vanished) {
        this.vanished = vanished;
    }

    public boolean isMuted() {
        return muted;
    }

    public void setMuted(final boolean muted) {
        this.muted = muted;
    }

    public boolean isGodmode() {
        return godmode;
    }

    public void setGodmode(final boolean godmode) {
        this.godmode = godmode;
    }

    public long getLastOnline() {
        return last_online;
    }

    public void setLastOnline(final long last_online) {
        this.last_online = last_online;
    }

    public Location getLastLocation() {
        return last_location;
    }

    public void setLastLocation(final Location last_location) {
        this.last_location = last_location;
    }

    public long getTimePlayed() {
        return timeplayed;
    }

    public void setTimePlayed(final long timeplayed) {
        this.timeplayed = timeplayed;
    }

    public void save() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM playerdata WHERE id = (?)", uuid.toString());
        db.execute("INSERT INTO playerdata (id, nick, arrowtrail, playertrail, vanished, muted, godmode, " +
                        "lastOnline, lastLocation, timeplayed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                uuid.toString(), nickname, arrowtrail, playertrail, vanished, muted, godmode,
                last_online, VRNUtil.LocationSerialization.serialize(last_location), timeplayed);
    }
}
