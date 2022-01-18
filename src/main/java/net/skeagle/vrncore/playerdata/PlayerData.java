package net.skeagle.vrncore.playerdata;

import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.trail.TrailType;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.UUID;

import static net.skeagle.vrncommands.BukkitUtils.color;

public class PlayerData {

    private final UUID uuid;
    private String nickname;
    private final TrailData playerTrailData;
    private final TrailData arrowTrailData;
    private final PlayerStates states;
    private long timePlayed;

    PlayerData(final UUID uuid) {
        this.uuid = uuid;
        this.playerTrailData = new TrailData(TrailType.PLAYER);
        this.arrowTrailData = new TrailData(TrailType.ARROW);
        this.states = new PlayerStates();
    }

    PlayerData(final UUID uuid, final String nickname, final TrailData playerTrailData, final TrailData arrowTrailData, final PlayerStates states, final long timePlayed) {
        this.uuid = uuid;
        this.nickname = nickname;
        this.playerTrailData = playerTrailData;
        this.arrowTrailData = arrowTrailData;
        this.states = states;
        this.timePlayed = timePlayed;
    }

    public String getNick() {
        return nickname;
    }

    public void setNick(final String nickname) {
        this.nickname = nickname != null ? color(nickname + "&r") : null;
        updateName();
    }

    public String getName() {
        return nickname != null ? color(nickname + "&r") : getPlayer().getName();
    }

    public void updateName() {
        if (getPlayer() != null) {
            getPlayer().setDisplayName(getPlayer().getName());
            String listname = null;
            if (HookManager.isVaultLoaded()) {
                listname = HookManager.format(Settings.listFormat, getPlayer());
            }
            getPlayer().setPlayerListName(color(listname != null ? listname : getPlayer().getName()));
        }
    }

    public TrailData getPlayerTrailData() {
        return playerTrailData;
    }

    public TrailData getArrowTrailData() {
        return arrowTrailData;
    }

    public PlayerStates getStates() {
        return states;
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
        db.execute("INSERT INTO playerdata (id, nick, playerTrailData, arrowTrailData, playerStates, timePlayed) VALUES (?, ?, ?, ?, ?, ?)",
                uuid.toString(), nickname, VRNUtil.GSON.toJson(playerTrailData.serialize()), VRNUtil.GSON.toJson(arrowTrailData.serialize()), VRNUtil.GSON.toJson(states), timePlayed);
    }
}
