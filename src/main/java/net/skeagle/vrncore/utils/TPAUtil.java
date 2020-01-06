package net.skeagle.vrncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.mineacademy.fo.Common;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static org.mineacademy.fo.Valid.checkBoolean;

public class TPAUtil {
    private static TPAUtil util;
    private Map<Player, BukkitTask> tasks = new HashMap<>();
    private HashMap<UUID, UUID> StoredPlayer = new HashMap<>();
    private boolean tpahere;

    private TPAUtil() {

    }

    public static TPAUtil getUtil() {
        if (util == null) {
            util = new TPAUtil();
        }
        return util;
    }

    public void setTpahere(boolean tpahere) {
        this.tpahere = tpahere;
    }

    public boolean hasRequest(final Player p) {
        UUID uuid = StoredPlayer.get(p.getUniqueId());
        return uuid != null;
    }

    public boolean hasSentRequest(final Player p) {
        return StoredPlayer.containsKey(p.getUniqueId());
    }

    public Player getStoredPlayer(final Player p) {
        if (hasRequest(p)) {
            UUID uuid = StoredPlayer.get(p.getUniqueId());
            return Bukkit.getPlayer(uuid);
        }
        say(p, "&cThat player is not online.");
        return null;
    }

    public void addPlayers(final UUID u1, final UUID u2) {
        this.StoredPlayer.put(u1, u2);
    }

    public void teleportPlayer(final Player p, final Player a) {
        if (tpahere) {
            p.teleport(a);
            DelRequest(p.getUniqueId(), a.getUniqueId(), false);
            return;
        }
        a.teleport(p);
        DelRequest(p.getUniqueId(), a.getUniqueId(), false);
        DelTask(a);
    }

    public void DelRequest(final UUID u1, final UUID u2, boolean showmsg) {
        if (showmsg) {
            say(Bukkit.getPlayer(u2),"&cThe teleport request has expired.");
            say(Bukkit.getPlayer(u1),"&cThe teleport request from " + Bukkit.getPlayer(u2).getName() + " has expired.");
        }
        StoredPlayer.remove(u1, u2);
        DelTask(Bukkit.getPlayer(u2));
    }

    public void DelTPATimer(Player p, Player a) {
        tasks.put(p, Common.runTimer(20, () -> DelRequest(a.getUniqueId(), p.getUniqueId(), true)));
    }

    private void DelTask(Player p) {
        checkBoolean(tasks.containsKey(p));
        BukkitTask task = tasks.remove(p);
        task.cancel();
    }
}
