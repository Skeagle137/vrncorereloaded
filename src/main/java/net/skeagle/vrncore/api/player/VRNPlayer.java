package net.skeagle.vrncore.api.player;

import net.skeagle.vrncore.api.hook.HookManager;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

public class VRNPlayer {

    private final PlayerData data;
    private final UUID uuid;
    private Player p;
    private List<String> homes;
    private List<String> warps;


    public VRNPlayer(final Player p) {
        this.data = PlayerManager.getData(p.getUniqueId());
        this.uuid = p.getUniqueId();
        this.p = p;
    }

    public VRNPlayer(final UUID uuid) {
        this.data = PlayerManager.getData(uuid);
        this.uuid = uuid;
    }

    public Player getPlayer() {
        return p;
    }

    public String getName() {
        return data.getNickname() != null ? color(data.getNickname()) + "&r" : getPlayer().getName();
    }

    public void setName(final String nickname) {
        final String name = color(nickname + "&r");
        data.setNickname(name);
        p.setDisplayName(name);
        String listname = name;
        if (HookManager.isVaultLoaded()) {
            listname = "%prefix" + name + "%suffix";
            listname = HookManager.format(listname, p);
        }
        p.setPlayerListName(listname);
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
