package net.skeagle.vrncore.listeners;

import net.skeagle.vrncore.utils.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InvClickListener implements Listener {

    @EventHandler
    public void onClick(InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player)) {
            return;
        }

        Player player = (Player) e.getWhoClicked();
        UUID playerUUID = player.getUniqueId();
        UUID inventoryUUID = CustomInventory.openInventories.get(playerUUID);

        if (inventoryUUID != null) {
            e.setCancelled(true);
            CustomInventory gui = CustomInventory.getInventoriesByUUID().get(inventoryUUID);
            CustomInventory.InvAction action = gui.getActions().get(e.getSlot());

            if (action != null) {
                action.click(player);
            }
        }
    }
}
