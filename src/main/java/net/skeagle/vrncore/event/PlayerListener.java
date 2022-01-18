package net.skeagle.vrncore.event;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;


import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class PlayerListener implements Listener {

    @EventHandler
    public void onPlayerJoin(final PlayerJoinEvent e) {
        final PlayerData data = PlayerManager.getData(e.getPlayer().getUniqueId());
        data.updateName();
        if (!e.getPlayer().hasPlayedBefore() && Settings.joinLeaveEnabled) {
            e.setJoinMessage(BukkitMessages.msg("welcomeMsg").replaceAll("%player%", data.getName()));
            return;
        }
        if (Settings.joinLeaveEnabled) {
            e.setJoinMessage(BukkitMessages.msg("joinMsg").replaceAll("%player%", data.getName()));
            Task.syncDelayed(() -> say(e.getPlayer(), BukkitMessages.msg("returnMsg").replaceAll("%player%", data.getName())));
        }
        e.getPlayer().setPlayerListHeaderFooter(BukkitMessages.msg("tabListHeader").replaceAll("%player%", data.getName()),
                BukkitMessages.msg("tabListFooter").replaceAll("%player%", data.getName()));
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        if (Settings.joinLeaveEnabled)
            e.setQuitMessage(BukkitMessages.msg("leaveMsg").replaceAll("%player%", PlayerManager.getData(e.getPlayer().getUniqueId()).getName()));
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
        String msg = HookManager.format(e.getPlayer());
        msg = msg.replaceAll("%", "%%");
        msg = msg.replace("%%message", "%2$s");
        e.setFormat(color(msg));
    }

    @EventHandler
    public void onGodDamage(final EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player player)) return;
        if (PlayerManager.getData(player.getUniqueId()).getStates().hasGodmode())
            e.setCancelled(true);
    }

    @EventHandler
    public void onDeath(final PlayerDeathEvent e) {
        final PlayerData data = PlayerManager.getData(e.getEntity().getUniqueId());
        if (e.getDeathMessage() != null) {
            String message = e.getDeathMessage().replaceAll(e.getEntity().getName(), data.getName());
            if (!data.getStates().isVanished()) {
                e.setDeathMessage(message);
            }
            else {
                e.setDeathMessage(null);
                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (VRNUtil.hasVanishPriority(player, e.getEntity())) {
                        sayNoPrefix(player, "&r" + message);
                    }
                }
            }
        }
    }
}
