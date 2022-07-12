package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncommands.misc.FormatUtils;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class GivePlusGUI {

    public GivePlusGUI(Player player) {
        enum GivePlusMaterial {
            BARRIER, COMMAND_BLOCK, CHAIN_COMMAND_BLOCK, REPEATING_COMMAND_BLOCK,
            SPAWNER, STRUCTURE_VOID, STRUCTURE_BLOCK, JIGSAW, DEBUG_STICK
        }
        InventoryGUI gui = new InventoryGUI(9, "&9&l/give+");
        for (int i = 0; i < GivePlusMaterial.values().length; i++) {
            ItemStack item = new ItemStack(Material.valueOf(GivePlusMaterial.values()[i].toString()));
            gui.addButton(ItemButton.create(new ItemBuilder(item).setName("&7" + FormatUtils.toTitleCase(item.getType().name().replaceAll("_", " "))), e -> {
                player.closeInventory();
                player.getInventory().addItem(new ItemBuilder(item).setCount(64));
                say(player, "You have received your item(s)");
            }), i);
        }
        gui.open(player);
    }
}
