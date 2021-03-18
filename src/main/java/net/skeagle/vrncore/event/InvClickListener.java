package net.skeagle.vrncore.event;

import net.skeagle.vrncore.GUIs.exptrade.CustomInventory;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.UUID;

public class InvClickListener implements Listener {

    @EventHandler
    public void onClick(final InventoryClickEvent e) {

        if (!(e.getWhoClicked() instanceof Player))
            return;

        final Player player = (Player) e.getWhoClicked();
        final UUID playerUUID = player.getUniqueId();
        final UUID inventoryUUID = CustomInventory.openInventories.get(playerUUID);

        if (inventoryUUID != null) {
            e.setCancelled(true);
            final CustomInventory gui = CustomInventory.getInventoriesByUUID().get(inventoryUUID);
            final CustomInventory.InvAction action = gui.getActions().get(e.getSlot());

            if (action != null)
                action.click(player);
        }
    }
}
