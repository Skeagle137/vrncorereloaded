package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hooks.VaultHook;
import net.skeagle.vrncore.settings.Settings;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.Common;

import static net.skeagle.vrncore.utils.VRNUtil.*;

public class PlayerListener implements Listener {

    /************************
     * =====JOIN EVENT=====
     ************************/

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        e.setJoinMessage(null);
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        final String name = cache.getNickname() != null ? cache.getNickname() + "&r" : e.getPlayer().getName();
        if (!e.getPlayer().hasPlayedBefore()) {
            e.setJoinMessage(color("&e" + name + " &6has joined for the first time. Welcome, &e" + name + "&6!"));
            return;
        }
        if (cache.getNickname() != null) e.getPlayer().setDisplayName(color(cache.getNickname() + "&r"));
        final VaultHook hook = VaultHook.getInstance();
        if (hook == null) return;
        else {
            String listname = "%prefix" + name + "%suffix";
            listname = hook.format(listname, e.getPlayer());
            e.getPlayer().setPlayerListName(color(listname));
        }
        final String join = "&7[&b+&7] &5" + name + " &dhas joined.";
        Common.logNoPrefix(color(join));
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getUniqueId() != e.getPlayer().getUniqueId()) {
                sayNoPrefix(pl, name);
            }
        }
        say(e.getPlayer(), "&dWelcome back, &5" + name + "&d!");
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        final String quit = "&7[&c-&7] &5" + (cache.getNickname() != null ? cache.getNickname() : e.getPlayer().getName()) + "&r &dhas left.";
        e.setQuitMessage(color(quit));
        Common.logNoPrefix(color(quit));
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final PlayerCache cache = PlayerCache.getCache(p);
        if (cache.isMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
        if (!Settings.Chat.ENABLED) return;
        if (!p.hasPermission("vrn.chat.allow")) {
            e.setCancelled(true);
            say(p, "&cYou do not have permission to use the chat.");
        }
        if (p.hasPermission("vrn.chat.color")) {
            e.setMessage(color(e.getMessage()));
        }
        final VaultHook hook = VaultHook.getInstance();
        if (hook == null) return;
        String msg = hook.format(p);
        msg = msg.replaceAll("%", "%%");
        msg = msg.replace("%%message", "%2$s");
        e.setFormat(color(msg));
    }

    /**************************
     * =====VANISH EVENT=====
     **************************/

    @EventHandler
    public void onPlayerJoinVanished(final PlayerJoinEvent e) {
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        if (cache.isVanished()) {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
            }
        }
    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final PlayerCache cache = PlayerCache.getCache(p);
            if (cache.isGodmode()) {
                e.setCancelled(true);
            }
        }
    }

    /**************************
     * =====OTHER EVENTS=====
     **************************/

    @EventHandler
    public void WorldChange(final PlayerChangedWorldEvent e) {
        sayActionBar(e.getPlayer(), "&a&lCurrently in world: \"" + e.getPlayer().getWorld().getName() + ".\"");
    }
}
