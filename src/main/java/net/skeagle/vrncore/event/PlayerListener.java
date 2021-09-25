package net.skeagle.vrncore.event;

import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static net.skeagle.vrncore.utils.VRNUtil.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(final AsyncPlayerPreLoginEvent e) {
        PlayerManager.getData(e.getUniqueId());
        /*
        if (p.getBannedTime < System.currentTimeMillis() && p.getBannedTime != 0) {
            e.disallow(AsyncPlayerPreLoginEvent.Result.KICK_BANNED, color("&cYou are banned from this server."));
            e.setKickMessage(color("&cYou have been banned by &b" + p.getBanPlayer() + "&c for: \n &6" + p.getBanReason()));
        }
        */

    }

    /************************
     * =====JOIN EVENT=====
     ************************/

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final PlayerData data = PlayerManager.getData(e.getPlayer().getUniqueId());
        if (!e.getPlayer().hasPlayedBefore() && Settings.joinLeaveEnabled) {
            e.setJoinMessage(Messages.msg("welcomeMsg").replaceAll("%player%", data.getName()));
            return;
        }
        if (data.getNick() != null) {
            data.updateName();
        }
        if (Settings.joinLeaveEnabled) {
            e.setJoinMessage(Messages.msg("joinMsg").replaceAll("%player%", data.getName()));
            Task.syncDelayed(() -> say(e.getPlayer(), Messages.msg("returnMsg").replaceAll("%player%", data.getName())));
        }
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (Settings.joinLeaveEnabled)
            e.setQuitMessage(Messages.msg("leaveMsg").replaceAll("%player%", PlayerManager.getData(e.getPlayer().getUniqueId()).getName()));
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        if (!Settings.Chat.enabled)
            return;
        if (!e.getPlayer().hasPermission("vrn.chat.allow") && Settings.Chat.chatPermission) {
            e.setCancelled(true);
            say(e.getPlayer(), "&cYou do not have permission to use the chat.");
        }
        final PlayerData data = PlayerManager.getData(e.getPlayer().getUniqueId());
        if (data.isMuted()) {
            e.setCancelled(true);
            say(e.getPlayer(), "&cYou are muted. You cannot chat.");
        }
        if (e.getPlayer().hasPermission("vrn.chat.color") || !Settings.Chat.colorPermission)
            e.setMessage(color(e.getMessage()));
        if (!HookManager.isVaultLoaded())
            return;
        String msg = HookManager.format(e.getPlayer());
        msg = msg.replaceAll("%", "%%");
        msg = msg.replace("%%message", "%2$s");
        e.setFormat(color(msg));
    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (PlayerManager.getData(player.getUniqueId()).isGodmode())
            e.setCancelled(true);
    }
}
