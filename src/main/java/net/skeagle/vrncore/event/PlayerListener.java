package net.skeagle.vrncore.event;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
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
        if (!e.getPlayer().hasPlayedBefore()) {
            e.setJoinMessage(color("&e" + e.getPlayer().getName() + " &6has joined for the first time. Welcome, &e" + e.getPlayer().getName() + "&6!"));
            return;
        }
        if (cache.getNickname() != null) {
            e.getPlayer().setDisplayName(color(cache.getNickname() + "&r"));
            e.getPlayer().setPlayerListName(color(cache.getNickname() + "&r"));
        }
        Common.logNoPrefix(color("&7[&b+&7] &5" + (cache.getNickname() != null ? cache.getNickname() + "&r" : e.getPlayer().getName())) + " &dhas joined.");
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl.getUniqueId() != e.getPlayer().getUniqueId()) {
                sayNoPrefix(pl, "&7[&b+&7] &5" + (cache.getNickname() != null ? cache.getNickname() + "&r" : e.getPlayer().getName() + " &dhas joined."));
            }
        }
        say(e.getPlayer(), "&dWelcome back, &5" + (cache.getNickname() != null ? cache.getNickname() + "&r" : e.getPlayer().getName()) + "&d!");
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        e.setQuitMessage(color("&7[&c-&7] &5" + e.getPlayer().getName() + " &dhas left."));
        final PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        if (cache.getNickname() != null) {
            if (e.getQuitMessage() != null && !e.getQuitMessage().equals("")) {
                e.setQuitMessage(e.getQuitMessage().replaceAll(e.getPlayer().getName(), color(cache.getNickname() + "&r")));
            }
        }
    }

    /************************
     * =====MUTE EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final PlayerCache cache = PlayerCache.getCache(p);
        if (cache.isMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
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
