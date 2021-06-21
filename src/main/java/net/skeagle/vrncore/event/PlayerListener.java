package net.skeagle.vrncore.event;

import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.utils.VRNPlayer;
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
        final VRNPlayer p = new VRNPlayer(e.getUniqueId());
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
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        final String name = p.getName();
        e.getPlayer().setDisplayName(name);
        if (!e.getPlayer().hasPlayedBefore() && Settings.joinLeaveEnabled) {
            e.setJoinMessage(Messages.msg("welcomeMsg").replaceAll("%player%", name));
            return;
        }
        String listname = null;
        if (HookManager.isVaultLoaded()) {
            listname = "%prefix" + name + "%suffix";
            listname = HookManager.format(listname, e.getPlayer());
        }
        e.getPlayer().setPlayerListName(color(listname != null ? listname : name));
        if (Settings.joinLeaveEnabled) {
            e.setJoinMessage(Messages.msg("joinMsg").replaceAll("%player%", name));
            Task.syncDelayed(() -> say(e.getPlayer(), Messages.msg("returnMsg").replaceAll("%player%", name)));
        }
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (Settings.joinLeaveEnabled)
            e.setQuitMessage(Messages.msg("leaveMsg").replaceAll("%player%", p.getName()));
        p.save();
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        System.out.println("bruh?");
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (p.isMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
        if (!Settings.Chat.enabled)
            return;
        if (!hasPerm(p, "vrn.chat.allow") && Settings.Chat.chatPermission) {
            e.setCancelled(true);
            say(p, "&cYou do not have permission to use the chat.");
        }
        if (hasPerm(p, "vrn.chat.color") || !Settings.Chat.colorPermission)
            e.setMessage(color(e.getMessage()));
        if (!HookManager.isVaultLoaded())
            return;
        String msg = HookManager.format(p.getPlayer());
        msg = msg.replaceAll("%", "%%");
        msg = msg.replace("%%message", "%2$s");
        System.out.println("bruh?");
        e.setFormat(color(msg));
    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (new VRNPlayer((Player) e.getEntity()).isGodmode())
            e.setCancelled(true);
    }
}
