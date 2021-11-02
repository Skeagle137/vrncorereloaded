package net.skeagle.vrncore.playerdata;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.trail.style.TrailStyle;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import javax.annotation.Nullable;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class PlayerData {

    private final UUID uuid;
    private String nickname;
    private Particle arrowtrail;
    private Particle playertrail;
    private TrailStyle trailStyle;
    private boolean vanished;
    private boolean muted;
    private boolean godmode;
    private Location lastLocation;
    private long timePlayed;

    PlayerData(final UUID uuid, final String nickname, final Particle arrowtrail, final Particle playertrail,
               final TrailStyle trailStyle, final boolean vanished, final boolean muted, final boolean godmode,
               final Location lastLocation, final long timePlayed) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.arrowtrail = arrowtrail;
        this.playertrail = playertrail;
        this.trailStyle = trailStyle;
        this.vanished = vanished;
        this.muted = muted;
        this.godmode = godmode;
        this.lastLocation = lastLocation;
        this.timePlayed = timePlayed;
    }

    public String getNick() {
        return nickname;
    }

    public void setNick(final String nickname) {
        this.nickname = color(nickname + "&r");;
        updateName();
    }

    public String getName() {
        return nickname != null ? color(nickname + "&r") : getPlayer().getName();
    }

    public void updateName() {
        if (getPlayer() != null) {
            getPlayer().setDisplayName(nickname);
            String listname = nickname;
            if (HookManager.isVaultLoaded()) {
                listname = "%prefix" + nickname + "%suffix";
                listname = HookManager.format(listname, getPlayer());
            }
            getPlayer().setPlayerListName(color(listname));
        }
    }

    public Particle getArrowTrail() {
        return arrowtrail;
    }

    public void setArrowTrail(@Nullable final Particle arrowtrail) {
        this.arrowtrail = arrowtrail;
    }

    public Particle getPlayerTrail() {
        return playertrail;
    }

    public void setPlayerTrail(@Nullable final Particle playertrail) {
        this.playertrail = playertrail;
    }

    public TrailStyle getTrailStyle() {
        return trailStyle;
    }

    public void setTrailStyle(TrailStyle trailStyle) {
        this.trailStyle = trailStyle;
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

    public Location getLastLocation() {
        return lastLocation;
    }

    public void setLastLocation(final Location last_location) {
        this.lastLocation = last_location;
    }

    public long getTimePlayed() {
        return timePlayed;
    }

    public void setTimePlayed(final long timeplayed) {
        this.timePlayed = timeplayed;
    }

    public Player getPlayer() {
        return Bukkit.getPlayer(uuid);
    }

    public void save() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        db.execute("DELETE FROM playerdata WHERE id = (?)", uuid.toString());
        db.execute("INSERT INTO playerdata (id, nick, arrowtrail, playertrail, trailStyle, vanished, muted, " +
                        "godmode, lastLocation, timePlayed) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                uuid.toString(), nickname, arrowtrail, playertrail, VRNcore.getInstance().getStyleRegistry().getStyle(trailStyle), vanished, muted,
                godmode, VRNUtil.LocationSerialization.serialize(lastLocation), timePlayed);
    }
}
