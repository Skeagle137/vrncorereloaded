package net.skeagle.vrncore.GUIs;

import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.inventorygui.PaginationPanel;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class BorderedGUI extends InventoryGUI {

    public BorderedGUI(String title) {
        super(54, title);
        fill(0, 54, InventoryGUI.FILLER);
        fill(1, 1, 8, 5, new ItemStack(Material.AIR));
    }

    public PaginationPanel paginate(ItemButton backButton, ItemButton infoButton) {
        PaginationPanel panel = new PaginationPanel(this);
        panel.addSlots(1, 1, 8, 5);
        panel.setOnUpdate(() -> {
            if (panel.getMaxPage() > 1) {
                addButton(47, ItemButton.create(new ItemBuilder(getPrevIcon(panel)).setName("&e&lBack"), e -> panel.prevPage()));
                addButton(51, ItemButton.create(new ItemBuilder(getNextIcon(panel)).setName("&e&lNext"), e -> panel.nextPage()));
            }
        });
        if (backButton != null) {
            addButton(45, backButton);
        }
        if (infoButton != null) {
            addButton(49, infoButton);
        }
        return panel;
    }

    public PaginationPanel paginate() {
        return paginate(null, null);
    }

    private Material getPrevIcon(PaginationPanel panel) {
        return panel.getPage() == 1 ? Material.GRAY_DYE : Material.PURPLE_DYE;
    }

    private Material getNextIcon(PaginationPanel panel) {
        return panel.getPage() == panel.getMaxPage() ? Material.GRAY_DYE : Material.PINK_DYE;
    }
}
