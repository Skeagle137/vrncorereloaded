package net.skeagle.vrncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public abstract class CustomInventory {

    private UUID uuid;

    private Inventory inv;
    private Map<Integer, InvAction> actions;
    private static Map<UUID, CustomInventory> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();

    public CustomInventory(int size, String title) {

        this.uuid = UUID.randomUUID();

        this.inv = Bukkit.createInventory(null, size, ChatColor.translateAlternateColorCodes('&', title));

        this.actions = new HashMap<>();
        inventoriesByUUID.put(getUUID(), this);
    }

    public interface InvAction {
        void click(Player player);
    }

    protected void setItem(int slot, ItemStack stack, InvAction action, String name, String[] lore) {

        ItemMeta meta = stack.getItemMeta();
        List<String> loreList = new ArrayList<>();

        for (String s : lore) {
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

    public void open(Player p) {
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
