package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.playerdata.PlayerStates;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TpCommands {

    private final TpaUtil tpaUtil;
    private final BackCache backCache;

    public TpCommands() {
        tpaUtil = new TpaUtil();
        backCache = new BackCache();
        new EventListener<>(VRNCore.getInstance(), PlayerTeleportEvent.class, e -> {
            if (e.getCause() == PlayerTeleportEvent.TeleportCause.COMMAND || e.getCause() == PlayerTeleportEvent.TeleportCause.PLUGIN)
                backCache.setBackLoc(e.getPlayer().getUniqueId(), e.getFrom());
        });
    }

    @CommandHook("tpall")
    public void onTpAll(final Player player) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != player)
                onTpHere(player, pl);
        }
        say(player, BukkitMessages.msg("teleportedAll"));
    }

    @CommandHook("tphere")
    public void onTpHere(final Player player, final Player target) {
        sayTp(player);
        target.teleport(player.getLocation());
    }

    @CommandHook("top")
    public void onTop(final Player player) {
        final int y;
        final Block b = VRNUtil.getStandingBlock(player.getLocation());
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
        final Location backLoc = backCache.getBackLoc(target.getUniqueId());
        if (backLoc == null) {
            say(player, target == player ? "&cYou do not have anywhere to teleport back to."
                    : "&a" + target.getName() + " &7does not have a saved last location.");
            return;
        }
        final Location newLoc = player.getLocation();
        backCache.teleToBackLoc(player, target);
        backCache.setBackLoc(player.getUniqueId(), newLoc);
        say(player, target == player ? "&7Teleported to your last location."
                : "&7Teleported to &a" + target.getName() + "&7's last location.");
        backCache.setBackLoc(player.getUniqueId(), newLoc);
    }

    @CommandHook("tpa")
    public void onTpa(final Player player, final Player target) {
        if (player == target) {
            say(player, "&cYou cannot teleport to yourself.");
            return;
        }
        if (tpaUtil.getRequestWhereSender(player) != null) {
            say(player, "&cYou already have a pending teleport request.");
            return;
        }
        tpaUtil.addRequest(player, target, TpaUtil.RequestType.THERE).thenAccept(sent -> {
            if (!sent) {
                say(player, "&c" + target.getName() + " has teleport requests disabled.");
                return;
            }
            say(player, "Teleport request sent.");
            say(target, "&a" + player.getName() + " &7is requesting to teleport to you. " +
                    "Do /tpaccept to accept the request or /tpdeny to deny it. This request will expire in 2 minutes.");
        });
    }

    @CommandHook("tpdeny")
    public void onTpdeny(final Player player) {
        if (tpaUtil.getRequestWhereReciever(player) == null) {
            say(player, "&cYou do not have any pending teleport requests.");
            return;
        }
        final TpaUtil.TpaRequest request = tpaUtil.getRequestWhereReciever(player);
        final OfflinePlayer sender = Bukkit.getOfflinePlayer(request.sender);
        say(player, "&7Denied the teleport request from &a" + sender.getName() + "&7.");
        if (sender.isOnline())
            say((Player) sender, "&cYour teleport request was denied.");
        tpaUtil.deleteRequest(request, false);
    }

    @CommandHook("tpaccept")
    public void onTpaccept(final Player player) {
        if (tpaUtil.getRequestWhereReciever(player) == null) {
            say(player, "&cYou do not have any pending teleport requests.");
            return;
        }
        final TpaUtil.TpaRequest request = tpaUtil.getRequestWhereReciever(player);
        final OfflinePlayer sender = Bukkit.getOfflinePlayer(request.sender);
        if (!sender.isOnline()) {
            say(player, "&cCould not teleport " + sender.getName() + " since they are offline.");
            return;
        }
        tpaUtil.teleportPlayers(request);
        say(player, "&7Accepted the teleport request from &a" + sender.getName() + "&7.");
    }

    @CommandHook("tpahere")
    public void onTpaHere(final Player player, final Player target) {
        if (player == target) {
            say(player, "&cYou cannot teleport to yourself.");
            return;
        }
        if (tpaUtil.getRequestWhereSender(player) != null) {
            say(player, "&cYou already have a pending teleport request.");
            return;
        }
        tpaUtil.addRequest(player, target, TpaUtil.RequestType.HERE).thenAccept(sent -> {
            if (!sent) {
                say(player, "&c" + target.getName() + " has teleport requests disabled.");
                return;
            }
            say(player, "Teleport request sent.");
            say(target, "&a" + player.getName() + " &7is requesting for you to teleport to them. " +
                    "Do /tpaccept to accept the request or /tpdeny to deny it. This request will expire in 2 minutes.");
        });
    }

    @CommandHook("tpcancel")
    public void onTpcancel(final Player player) {
        if (tpaUtil.getRequestWhereSender(player) == null) {
            say(player, "&cYou already have a pending teleport request.");
            return;
        }
        final TpaUtil.TpaRequest request = tpaUtil.getRequestWhereSender(player);
        final OfflinePlayer reciever = Bukkit.getOfflinePlayer(request.reciever);
        if (reciever.isOnline())
            say((Player) reciever, "&c" + player.getName() + " cancelled their teleport request sent to you.");
        tpaUtil.deleteRequest(request, false);
        say(player, "Cancelled the teleport request.");
    }

    @CommandHook("tptoggle")
    public void onTpToggle(final Player player) {
        VRNCore.getPlayerData(player.getUniqueId()).thenAccept(data -> {
            PlayerStates states = data.getStates();
            states.setTpDisabled(!states.isTpDisabled());
            say(player, "Teleport requests are now &a" + (states.isTpDisabled() ? "disabled" : "enabled") + "&7.");
        });
    }

    private void sayTp(final CommandSender sender) {
        say(sender, BukkitMessages.msg("teleporting"));
    }

    private static class BackCache {
        private final Map<UUID, Location> backLoc = new HashMap<>();

        Location getBackLoc(final UUID id) {
            return backLoc.get(id);
        }

        void setBackLoc(final UUID id, final Location loc) {
            backLoc.remove(id);
            backLoc.put(id, loc);
        }

        void teleToBackLoc(final Player p, final Player targetLoc) {
            p.teleport(backLoc.get(targetLoc.getUniqueId()));
        }
    }

    private static class TpaUtil {
        private final Map<TpaRequest, Task> requests = new HashMap<>();

        TpaRequest getRequestWhereReciever(final Player p) {
            return requests.keySet().stream().filter(r -> r.reciever.equals(p.getUniqueId()) && r.active).findFirst().orElse(null);
        }

        TpaRequest getRequestWhereSender(final Player p) {
            return requests.keySet().stream().filter(r -> r.sender.equals(p.getUniqueId())).findFirst().orElse(null);
        }

        private CompletableFuture<Boolean> addRequest(final Player player, final Player target, final RequestType type) {
            return VRNCore.getPlayerData(player.getUniqueId()).thenApply(data -> {
                PlayerStates states = data.getStates();
                if (states.isTpDisabled()) {
                    return false;
                }
                this.sendRequest(player, target, type);
                return true;
            });
        }

        private void sendRequest(Player player, Player target, RequestType type) {
            final TpaRequest r = getRequestWhereReciever(target);
            if (r != null)
                r.active = false;
            final TpaRequest request = new TpaRequest(player, target, type);
            requests.put(request, Task.syncDelayed(() -> deleteRequest(request, true), 20 * 120));
        }

        void teleportPlayers(final TpaRequest request) {
            final Player sender = Bukkit.getPlayer(request.sender);
            final Player reciever = Bukkit.getPlayer(request.reciever);
            if (request.type == RequestType.THERE)
                sender.teleport(reciever);
            else
                reciever.teleport(sender);
            deleteRequest(request, false);
        }

        void deleteRequest(final TpaRequest request, final boolean showmsg) {
            if (showmsg) {
                final OfflinePlayer sender = Bukkit.getOfflinePlayer(request.sender);
                final Player reciever = Bukkit.getPlayer(request.reciever);
                if (sender.isOnline())
                    say((Player) sender, "&cThe teleport request has expired.");
                if (reciever != null)
                    say(reciever, "&cThe teleport request from " + sender.getName() + " has expired.");
            }
            requests.get(request).cancel();
            requests.remove(request);
        }

        private enum RequestType {
            THERE,
            HERE
        }

        private static class TpaRequest {
            private final UUID sender;
            private final UUID reciever;
            private final RequestType type;
            private boolean active;

            TpaRequest(final Player sender, final Player reciever, final RequestType type) {
                this.sender = sender.getUniqueId();
                this.reciever = reciever.getUniqueId();
                this.type = type;
                this.active = true;
            }
        }
    }
}
