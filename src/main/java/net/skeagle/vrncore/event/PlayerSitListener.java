package net.skeagle.vrncore.event;

import net.skeagle.vrncore.utils.PlayerSitUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

public class PlayerSitListener implements Listener {

    @EventHandler
    public void onPlayerDeath(final PlayerDeathEvent e) {
        handleSit(e.getEntity());
    }

    @EventHandler
    public void onPlayerQuit(final PlayerQuitEvent e) {
        handleSit(e.getPlayer());
    }

    @EventHandler
    public void onPlayerTeleport(final PlayerTeleportEvent e) {
        handleSit(e.getPlayer());
    }

    @EventHandler
    public void onArmorStandManipulate(final PlayerArmorStandManipulateEvent e) {
        if (PlayerSitUtil.containsStand(e.getRightClicked()))
            e.setCancelled(true);
    }

    private void handleSit(Player p) {
        final PlayerSitUtil util = PlayerSitUtil.getPlayer(p);
        if (util.isSitting())
            util.setSitting(false);
    }
}
