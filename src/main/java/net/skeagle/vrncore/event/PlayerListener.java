package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.utils.VRNPlayer;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.*;

import static net.skeagle.vrncore.utils.VRNUtil.*;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerPreLogin(AsyncPlayerPreLoginEvent e) {
        VRNPlayer p = new VRNPlayer(e.getUniqueId());
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
    public void onPlayerJoin(PlayerJoinEvent e) {
        VRNPlayer p = new VRNPlayer(e.getPlayer());
        String name = p.getName();
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
    public void onPlayerQuit(PlayerQuitEvent e) {
        VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (Settings.joinLeaveEnabled)
            e.setQuitMessage(Messages.msg("leaveMsg").replaceAll("%player%", p.getName()));
        p.save();
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (p.isMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
        if (!Settings.Chat.enabled)
            return;
        if (!hasPerm(p, "vrn.chat.allow") && Settings.Chat.chatPermission || hasPerm(p, "vrn.chat.allow")) {
            e.setCancelled(true);
            say(p, "&cYou do not have permission to use the chat.");
        }
        if (!hasPerm(p, "vrn.chat.color") && Settings.Chat.colorPermission || hasPerm(p, "vrn.chat.color"))
            e.setMessage(color(e.getMessage()));
        if (!HookManager.isVaultLoaded())
            return;
        String msg = HookManager.format(p.getPlayer());
        msg = msg.replaceAll("%", "%%");
        msg = msg.replace("%%message", "%2$s");
        e.setFormat(color(msg));
    }

    /**************************
     * =====VANISH EVENTS=====
     **************************/

    @EventHandler
    public void onPlayerJoinVanished(PlayerJoinEvent e) {
        VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (p.isVanished())
            for (Player pl : Bukkit.getOnlinePlayers())
                pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
    }

    @EventHandler
    public void onEntityTarget(EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player)) return;
        VRNPlayer p = new VRNPlayer((Player) e.getTarget());
        if (p.isVanished()) {
            e.setCancelled(true);
            e.setTarget(null);
        }
        if (e.getEntity() instanceof Mob)
            ((Mob) e.getEntity()).setTarget(null);

    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        if (new VRNPlayer((Player) e.getEntity()).isGodmode())
            e.setCancelled(true);
    }

    /**************************
     * =====OTHER EVENTS=====
     **************************/

    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent e) {
        if (e.getPlayer().hasPermission("vrn.worldnotify"))
            sayActionBar(e.getPlayer(), "&a&lCurrently in world: \"" + e.getPlayer().getWorld().getName() + ".\"");
    }
}
