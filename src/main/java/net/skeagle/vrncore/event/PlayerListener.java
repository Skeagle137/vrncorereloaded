package net.skeagle.vrncore.event;

import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
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

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class PlayerListener implements Listener {

    /************************
     * =====JOIN EVENT=====
     ************************/

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        e.setJoinMessage(null);
        Player p = e.getPlayer();
        for (Player pl : Bukkit.getServer().getOnlinePlayers()) {
            if (!pl.getName().equals(p.getName())) {
                Bukkit.broadcastMessage(color("&7[&b+&7] &5" + p.getName() + " &dhas joined."));
                if (!p.hasPlayedBefore()) {
                    say(pl,"&e" + p.getName() + " &6has joined for the first time. Welcome, &e" + p.getName() + "&6!");
                    Common.log("&e" + p.getName() + " &6has joined for the first time. Welcome, &e" + p.getName() + "&6!");
                }
            } else {
                if (!p.hasPlayedBefore()) {
                    say(p, "&dWelcome to &b&lVRN Network&r&d, &5" + p.getName() + "&d!");
                    return;
                }
                say(p, "&dWelcome back, &5" + p.getName() + "&d!");
            }
        }
    }

    /************************
     * =====QUIT EVENT=====
     ************************/

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e)
    {
        e.setQuitMessage(null);
        Bukkit.broadcastMessage(color("&7[&c-&7] &5" + e.getPlayer().getName() + " &dhas left."));
    }

    /************************
     * =====MUTE EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        PlayerCache cache = PlayerCache.getCache(p);
        if (cache.isMuted()) {
            e.setCancelled(true);
            say(p,"&cYou are muted. You cannot chat.");
        }
    }

    /**************************
     * =====VANISH EVENT=====
     **************************/

    @EventHandler
    public void onPlayerJoinVanished(PlayerJoinEvent e) {
        PlayerCache cache = PlayerCache.getCache(e.getPlayer());
        if (cache.isVanished()) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
            }
        }
    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            PlayerCache cache = PlayerCache.getCache(p);
            if (cache.isGodmode()) {
                e.setCancelled(true);
            }
        }
    }

    /**************************
     * =====OTHER EVENTS=====
     **************************/

    @EventHandler
    public void WorldChange(PlayerChangedWorldEvent e) {
        e.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR,
                TextComponent.fromLegacyText(color("&a&lCurrently in world: \"" + e.getPlayer().getWorld().getName() + ".\"")));
    }

}
