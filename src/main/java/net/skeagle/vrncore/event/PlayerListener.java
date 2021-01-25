package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.hooks.VaultHook;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
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
        final PlayerData data = PlayerManager.getData(e.getPlayer());
        final String name = (data.getNickname() != null ? data.getNickname() + "&r" : e.getPlayer().getName());
        e.getPlayer().setDisplayName(color(name));
        if (!e.getPlayer().hasPlayedBefore() && Settings.Joining.ENABLED) {
            e.setJoinMessage(color(Settings.Joining.WELCOME.replaceAll("%player%", name)));
            return;
        }
        String listname = null;
        final VaultHook hook = VaultHook.getInstance();
        if (hook != null) {
            listname = "%prefix" + name + "%suffix";
            listname = hook.format(listname, e.getPlayer());
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
        final PlayerData data = PlayerManager.getData(e.getPlayer());
        final String name = data.getNickname() != null ? data.getNickname() + "&r" : e.getPlayer().getName();
        if (Settings.Joining.ENABLED)
            e.setQuitMessage(color(Settings.Joining.QUIT.replaceAll("%player%", name)));
    }

    /************************
     * =====CHAT EVENT=====
     ************************/

    @EventHandler
    public void onAsyncPlayerChat(final AsyncPlayerChatEvent e) {
        final Player p = e.getPlayer();
        final PlayerData data = PlayerManager.getData(p);
        if (data.getMuted()) {
            e.setCancelled(true);
            say(p, "&cYou are muted. You cannot chat.");
        }
        if (!Settings.Chat.ENABLED)
            return;
        if (!p.hasPermission("vrn.chat.allow")) {
            e.setCancelled(true);
            say(p, "&cYou do not have permission to use the chat.");
        }
        if (p.hasPermission("vrn.chat.color"))
            e.setMessage(color(e.getMessage()));
        final VaultHook hook = VaultHook.getInstance();
        if (hook == null)
            return;
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
        final PlayerData data = PlayerManager.getData(e.getPlayer());
        if (data.getVanished()) {
            for (final Player pl : Bukkit.getOnlinePlayers())
                pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
        }
    }

    /***********************
     * =====GOD EVENT=====
     ***********************/

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final PlayerData data = PlayerManager.getData(p);
            if (data.getGodmode())
                e.setCancelled(true);
        }
    }

    /**************************
     * =====OTHER EVENTS=====
     **************************/

    @EventHandler
    public void WorldChange(final PlayerChangedWorldEvent e) {
        e.getPlayer().hasPermission("vrn.worldchange");
        sayActionBar(e.getPlayer(), "&a&lCurrently in world: \"" + e.getPlayer().getWorld().getName() + ".\"");
    }
}
