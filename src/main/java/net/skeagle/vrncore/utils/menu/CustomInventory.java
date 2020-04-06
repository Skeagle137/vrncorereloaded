package net.skeagle.vrncore.utils.menu;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class CustomInventory {

    private final UUID uuid;

    private final Inventory inv;
    private final Map<Integer, InvAction> actions;
    private static final Map<UUID, CustomInventory> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();

    public CustomInventory(final int size, final String title) {

        this.uuid = UUID.randomUUID();

        this.inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));

        this.actions = new HashMap<>();
        inventoriesByUUID.put(getUUID(), this);
    }

    public interface InvAction {
        void click(Player player);
    }

    protected void setItem(final int slot, final ItemStack stack, final InvAction action, final String name, final String[] lore) {

        final ItemMeta meta = stack.getItemMeta();
        final List<String> loreList = new ArrayList<>();

        for (final String s : lore) {
            loreList.add(ChatColor.translateAlternateColorCodes('&', s));
        }

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', name));
        meta.setLore(loreList);

        stack.setItemMeta(meta);

        inv.setItem(slot, stack);
        if (action != null) {
            actions.put(slot, action);
        }
    }

    public void open(final Player p) {
        p.openInventory(inv);
        openInventories.put(p.getUniqueId(), getUUID());
    }

    private UUID getUUID() {
        return uuid;
    }

    public static Map<UUID, CustomInventory> getInventoriesByUUID() {
        return inventoriesByUUID;
    }

    public Map<Integer, InvAction> getActions() {
        return actions;
    }
}
