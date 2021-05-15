package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.hook.HookManager;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.*;
import org.mineacademy.fo.Common;

import static net.skeagle.vrncore.api.util.VRNUtil.*;

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
        if (!e.getPlayer().hasPlayedBefore() && Settings.Joining.ENABLED) {
            e.setJoinMessage(color(Settings.Joining.WELCOME.replaceAll("%player%", name)));
            return;
        }
        String listname = null;
        if (HookManager.isVaultLoaded()) {
            listname = "%prefix" + name + "%suffix";
            listname = HookManager.format(listname, e.getPlayer());
        }
        e.getPlayer().setPlayerListName(color(listname != null ? listname : name));
        if (Settings.Joining.ENABLED) {
            e.setJoinMessage(color(Settings.Joining.JOIN.replaceAll("%player%", name)));
            Common.runLater(() -> say(e.getPlayer(), Settings.Joining.RETURN.replaceAll("%player%", name)));
        }
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (Settings.Joining.ENABLED)
            e.setQuitMessage(color(Settings.Joining.QUIT.replaceAll("%player%", p.getName())));
        p.save();
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (p.isMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
        if (!Settings.Chat.ENABLED)
            return;
        if (!hasPerm(p, "vrn.chat.allow")) {
            e.setCancelled(true);
            say(p, "&cYou do not have permission to use the chat.");
        }
        if (hasPerm(p, "vrn.chat.color"))
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
    public void onPlayerJoinVanished(final PlayerJoinEvent e) {
        final VRNPlayer p = new VRNPlayer(e.getPlayer());
        if (p.isVanished())
            for (final Player pl : Bukkit.getOnlinePlayers())
                pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
    }

    @EventHandler
    public void onEntityTarget(final EntityTargetEvent e) {
        if (!(e.getTarget() instanceof Player)) return;
        final VRNPlayer p = new VRNPlayer((Player) e.getTarget());
        if (p.isVanished()) {
            e.setCancelled(true);
            e.setTarget(null);
        }
        if (e.getEntity() instanceof Mob) {
            final Mob mob = (Mob) e.getEntity();
            mob.setTarget(null);
        }

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

    /**************************
     * =====OTHER EVENTS=====
     **************************/

    @EventHandler
    public void WorldChange(final PlayerChangedWorldEvent e) {
        hasPerm(e.getPlayer(), "vrn.worldchange");
        sayActionBar(e.getPlayer(), "&a&lCurrently in world: \"" + e.getPlayer().getWorld().getName() + ".\"");
    }
}
