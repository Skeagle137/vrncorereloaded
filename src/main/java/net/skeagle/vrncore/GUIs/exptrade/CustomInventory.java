package net.skeagle.vrncore.GUIs.exptrade;

import net.skeagle.vrncore.api.util.ItemUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

public abstract class CustomInventory {

    private final UUID uuid;

    private final Inventory inv;
    private final Map<Integer, InvAction> actions;
    private static final Map<UUID, CustomInventory> inventoriesByUUID = new HashMap<>();
    public static Map<UUID, UUID> openInventories = new HashMap<>();

    public CustomInventory(final int size, final String title) {

        this.uuid = UUID.randomUUID();

        this.inv = Bukkit.createInventory(null, size, color(title));

        this.actions = new HashMap<>();
        inventoriesByUUID.put(getUUID(), this);
    }

    public interface InvAction {
        void click(Player player);
    }

    protected void setItem(final int slot, final ItemStack stack, final InvAction action, final String name, final String... lore) {
        ItemUtil.Builder item = ItemUtil.genItem(stack).name(name);
        if (lore.length > 0)
            item.lore(Arrays.asList(lore));
        inv.setItem(slot, item.build());
        if (action != null)
            actions.put(slot, action);
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
