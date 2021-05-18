package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class TpCommands {

    public TpCommands() {
        new EventListener<>(VRNcore.getInstance(), PlayerTeleportEvent.class, e -> {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN) {
                final BackCache back = new BackCache();
                back.setBackLoc(e.getPlayer().getUniqueId(), e.getFrom());
            }
        });
    }

    @CommandHook("tpall")
    public void onTpAll(final Player player) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != player)
                onTpHere(player, pl);
        }
        say(player, Messages.msg("teleportedAll"));
    }

    @CommandHook("tphere")
    public void onTpHere(final Player player, final Player target) {
        sayTp(player);
        target.teleport(player.getLocation());
    }

    @CommandHook("top")
    public void onTop(final Player player) {
        final int y;
        final Block b = VRNUtil.getBlockExact(player.getLocation());
        if (b != null)
            y = player.getWorld().getHighestBlockYAt(b.getLocation());
        else
            y = player.getWorld().getHighestBlockYAt(player.getLocation());
        sayTp(player);
        final Location loc = player.getLocation().clone();
        loc.setY(y + 1);
        player.teleport(loc);
    }

    @CommandHook("back")
    public void onBack(final Player player, final Player target) {
        final Player backPlayer = target != null ? target : player;
        final BackCache back = new BackCache();
        final Location backLoc = back.getBackLoc(backPlayer.getUniqueId());
        if (backLoc == null) {
            say(player, backPlayer == player ? "&cYou do not have anywhere to teleport back to."
                    : "&a" + backPlayer.getName() + " &7does not have a saved last location.");
            return;
        }
        final Location newLoc = player.getLocation();
        back.teleToBackLoc(player, backPlayer);
        back.setBackLoc(player.getUniqueId(), newLoc);
        say(player, backPlayer == player ? "&7Teleported to your last location."
                : "&7Teleported to &a" + backPlayer.getName() + "&7's last location.");
        back.setBackLoc(player.getUniqueId(), newLoc);
    }

    @CommandHook("tpa")
    public void onTpa(final Player player, final Player target) {
        if (player == target) {
            say(player, "&cYou cannot teleport to yourself.");
            return;
        }
        final TpaUtil util = new TpaUtil();
        if (util.hasRequest(player)) {
            say(player, "&cYou already have a pending teleport request.");
            return;
        }
        util.setTpahere(false);
        say(player, "&aTeleport request sent.");
        say(target, "&a" + player.getName() + " &7is requesting to teleport to you. " +
                "Do /tpaccept to accept the request or /tpdeny to deny it. This request will expire in 2 minutes.");
        util.addPlayers(player, target);
        util.DelTPATimer(player, target);
    }

    @CommandHook("tpdeny")
    public void onTpdeny(final Player player) {
        final TpaUtil util = new TpaUtil();
        if (!util.hasRequest(player)) {
            say(player, "&cYou do not have any pending teleport requests.");
            return;
        }
        final Player target = util.getTpaPlayer(player);
        say(player, "&7Denied the teleport request from &a" + target.getName() + "&7.");
        if (target.isOnline())
            say(target, "&cYour teleport request was denied.");
        util.DelRequest(player, target, false);
    }

    @CommandHook("tpaccept")
    public void onTpaccept(final Player player) {
        final TpaUtil util = new TpaUtil();
        if (!util.hasRequest(player)) {
            say(player, "&cYou do not have any pending teleport requests.");
            return;
        }
        final Player target = util.getTpaPlayer(player);
        say(player, "&7Accepted the teleport request from &a" + target.getName() + "&7.");
        if (target.isOnline())
            say(target, "&aTeleport request accepted.");
        util.teleportPlayer(player, target);
    }

    @CommandHook("tpahere")
    public void onTpaHere(final Player player, final Player target) {
        if (player == target) {
            say(player, "&cYou cannot teleport to yourself.");
            return;
        }
        final TpaUtil util = new TpaUtil();
        if (util.hasRequest(player)) {
            say(player, "&cYou already have a pending teleport request.");
            return;
        }
        util.setTpahere(true);
        say(player, "&aTeleport request sent.");
        say(target, "&a" + player.getName() + " &7is requesting for you to teleport to them. " +
                "Do /tpaccept to accept the request or /tpdeny to deny it. This request will expire in 2 minutes.");
        util.addPlayers(player, target);
        util.DelTPATimer(player, target);
    }

    @CommandHook("tpcancel")
    public void onTpcancel(final Player player) {

    }

    public void sayTp(final Player player) {
        say(player, Messages.msg("teleporting"));
    }

    private static class BackCache {
        private static final Map<UUID, Location> backLoc = new HashMap<>();

        public Location getBackLoc(final UUID id) {
            return backLoc.get(id);
        }

        public void setBackLoc(final UUID id, final Location loc) {
            backLoc.remove(id);
            backLoc.put(id, loc);
        }

        public void teleToBackLoc(final Player p, final Player targetLoc) {
            p.teleport(backLoc.get(targetLoc.getUniqueId()));
        }
    }

    private static class TpaUtil {
        private final static Map<UUID, Task> tasks = new HashMap<>();
        private final static HashMap<UUID, UUID> tpaMap = new HashMap<>();
        private boolean tpahere;

        public void setTpahere(final boolean tpahere) {
            this.tpahere = tpahere;
        }

        public boolean hasRequest(final Player player) {
            return tpaMap.get(player.getUniqueId()) != null;
        }

        public Player getTpaPlayer(final Player p) {
            if (hasRequest(p))
                return Bukkit.getPlayer(tpaMap.get(p.getUniqueId()));
            return null;
        }

        public void addPlayers(final Player player, final Player target) {
            tpaMap.put(player.getUniqueId(), target.getUniqueId());
        }

        public void teleportPlayer(final Player player, final Player target) {
            if (tpahere)
                player.teleport(target);
            else
                target.teleport(player);
            DelRequest(player, target, false);
            DelTask(player);
        }

        public void DelRequest(final Player player, final Player target, final boolean showmsg) {
            if (showmsg) {
                if (player.isOnline())
                    say(player, "&cThe teleport request has expired.");
                if (target.isOnline())
                    say(target, "&cThe teleport request from " + player.getName() + " has expired.");
            }
            tpaMap.remove(player.getUniqueId(), target.getUniqueId());
            DelTask(player);
        }

        public void DelTPATimer(final Player player, final Player target) {
            tasks.put(player.getUniqueId(), Task.syncDelayed(VRNcore.getInstance(), () -> DelRequest(player, target, true), 20 * 120));
        }

        private static void DelTask(final Player p) {
            if (tasks.containsKey(p.getUniqueId())) {
                final Task task = tasks.remove(p.getUniqueId());
                task.cancel();
            }
        }
    }
}
