package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.utils.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.UUID;

public class InvCloseListener implements Listener {

    @EventHandler
    public void onClose(InventoryCloseEvent e){

        Player p = (Player) e.getPlayer();
        UUID playerUUID = p.getUniqueId();

        CustomInventory.openInventories.remove(playerUUID);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e){

        Player player = e.getPlayer();
        UUID playerUUID = player.getUniqueId();

        CustomInventory.openInventories.remove(playerUUID);
    }
}
