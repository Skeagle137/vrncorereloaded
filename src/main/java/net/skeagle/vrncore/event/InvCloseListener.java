package net.skeagle.vrncore.event;

import net.skeagle.vrncore.GUIs.exptrade.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class InvCloseListener implements Listener {

    @EventHandler
    public void onClose(final InventoryCloseEvent e){

        final Player p = (Player) e.getPlayer();
        final UUID playerUUID = p.getUniqueId();

        CustomInventory.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onQuit(final PlayerQuitEvent e){

        final Player player = e.getPlayer();
        final UUID playerUUID = player.getUniqueId();

        CustomInventory.openInventories.remove(playerUUID);
    }
}
