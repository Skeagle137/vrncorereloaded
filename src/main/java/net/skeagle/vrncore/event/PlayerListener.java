package net.skeagle.vrncore.event;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class PlayerListener implements Listener {

    private final VRNcore plugin;

    public PlayerListener(VRNcore plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerPreLogin(final AsyncPlayerPreLoginEvent e) {
        if (e.getLoginResult() != AsyncPlayerPreLoginEvent.Result.ALLOWED) return;
        plugin.getPlayerManager().getData(e.getUniqueId()).whenComplete((res, ex) -> {
            if (ex == null) {
                e.setLoginResult(AsyncPlayerPreLoginEvent.Result.ALLOWED);
                return;
            }
            e.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
        }).join();
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerJoin(final PlayerJoinEvent e) {
        plugin.getPlayerManager().getData(e.getPlayer().getUniqueId()).thenAccept(data -> {
            if (!e.getPlayer().hasPlayedBefore() && Settings.joinLeaveEnabled) {
                e.setJoinMessage(BukkitMessages.msg("welcomeMsg").replaceAll("%player%", data.getName()));
            }
            else if (Settings.joinLeaveEnabled) {
                e.setJoinMessage(BukkitMessages.msg("joinMsg").replaceAll("%player%", data.getName()));
                Task.syncDelayed(() -> say(e.getPlayer(), BukkitMessages.msg("returnMsg").replaceAll("%player%", data.getName())), 2);
            }
            Task.syncDelayed(() -> {
                data.updateName();
                e.getPlayer().setPlayerListHeaderFooter(BukkitMessages.msg("tabListHeader").replaceAll("%player%", data.getName()),
                        BukkitMessages.msg("tabListFooter").replaceAll("%player%", data.getName()));
            }, 2);
        });
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (Settings.joinLeaveEnabled) {
            plugin.getPlayerManager().getData(e.getPlayer().getUniqueId()).thenAccept(data ->
                    e.setQuitMessage(BukkitMessages.msg("leaveMsg").replaceAll("%player%", data.getName())));
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        if (!Settings.chatEnabled)
            return;
        if (!e.getPlayer().hasPermission("vrn.chat.allow") && Settings.chatPermission) {
            e.setCancelled(true);
            say(e.getPlayer(), "&cYou do not have permission to use the chat.");
        }
        if (e.getPlayer().hasPermission("vrn.chat.color") || !Settings.colorPermission)
            e.setMessage(color(e.getMessage()));
        if (!HookManager.isVaultLoaded())
            return;
        plugin.getPlayerManager().getData(e.getPlayer().getUniqueId()).thenAccept(data -> {
            String msg = HookManager.format(data);
            msg = msg.replaceAll("%", "%%");
            msg = msg.replace("%%message", "%2$s");
            e.setFormat(color(msg));
        });
    }

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        plugin.getPlayerManager().getData(player.getUniqueId()).thenAccept(data -> {
            if (data.getStates().hasGodmode()) {
                e.setCancelled(true);
            }
        });
    }
}
