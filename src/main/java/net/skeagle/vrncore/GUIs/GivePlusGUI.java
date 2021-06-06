package net.skeagle.vrncore.GUIs;

import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class GivePlusGUI {

    public GivePlusGUI(final Player player) {
        final InventoryGUI gui = new InventoryGUI(9, "&9&l/give+");
        for (int i = 0; i < GivePlusMaterial.values().length; i++) {
            final GivePlusMaterial value = GivePlusMaterial.values()[i];
            gui.addButton(ItemButton.create(new ItemBuilder(value.mat).setName("&7" + value.name), e -> {
                player.closeInventory();
                player.getInventory().addItem(new ItemStack(value.mat));
                say(player, "You have received your item(s)");
            }), i);
        }
        gui.open(player);
    }

    private enum GivePlusMaterial {
        BARRIER(Material.BARRIER, "Barrier"),
        CMDBLOCK(Material.COMMAND_BLOCK, "Command Block (Normal)"),
        CMD_CHAIN(Material.CHAIN_COMMAND_BLOCK, "Command Block (Chain)"),
        CMD_REPEAT(Material.REPEATING_COMMAND_BLOCK, "Command Block (Repeat)"),
        SPAWNER(Material.SPAWNER, "Spawner"),
        STRUCT_VOID(Material.STRUCTURE_VOID, "Structure Void"),
        STRUCT_BLOCK(Material.STRUCTURE_BLOCK, "Structure Block"),
        DEBUG_STICK(Material.DEBUG_STICK, "Debug Stick");

        private final Material mat;
        private final String name;

        GivePlusMaterial(final Material mat, final String name) {
            this.mat = mat;
            this.name = name;
        }
    }
}
