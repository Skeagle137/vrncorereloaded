package net.skeagle.vrncore.utils;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.playerdata.PlayerData;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class VRNPlayer {

    private final PlayerData data;
    private final UUID uuid;
    private Player player;


    public VRNPlayer(final Player player) {
        this.data = VRNcore.getInstance().getPlayerManager().getData(player.getUniqueId());
        this.uuid = player.getUniqueId();
        this.player = player;
        if (data.getLastLocation() == null)
            data.setLastLocation(player.getLocation());
    }

    public VRNPlayer(final UUID uuid) {
        this.data = VRNcore.getInstance().getPlayerManager().getData(uuid);
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return player;
    }

    public String getName() {
        return data.getNickname() != null ? color(data.getNickname()) + "&r" : getPlayer().getName();
    }

    public void setName(final String nickname) {
        final String name = color(nickname + "&r");
        data.setNickname(name);
        player.setDisplayName(name);
        String listname = name;
        if (HookManager.isVaultLoaded()) {
            listname = "%prefix" + name + "%suffix";
            listname = HookManager.format(listname, player);
        }
        player.setPlayerListName(color(listname));
    }

    public Particle getArrowTrail() {
        return data.getArrowtrail();
    }

    public void setArrowTrail(final Particle particle) {
        data.setArrowtrail(particle);
    }

    public Particle getPlayerTrail() {
        return data.getPlayertrail();
    }

    public void setPlayerTrail(final Particle particle) {
        data.setPlayertrail(particle);
    }

    public boolean isGodmode() {
        return data.isGodmode();
    }

    public void setGodmode(final boolean godmode) {
        data.setGodmode(godmode);
    }

    public boolean isMuted() {
        return data.isMuted();
    }

    public void setMuted(final boolean muted) {
        data.setMuted(muted);
    }

    public boolean isVanished() {
        return data.isVanished();
    }

    public void setVanished(final boolean vanished) {
        data.setVanished(vanished);
    }

    public long getTimePlayed() {
        return data.getTimeplayed();
    }

    public void setTimePlayed(final long timePlayed) {
        data.setTimeplayed(timePlayed);
    }

    public void save() {
        data.save();
    }
}
