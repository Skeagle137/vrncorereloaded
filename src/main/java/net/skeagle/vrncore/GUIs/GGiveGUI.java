package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.ItemUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

import java.util.ArrayList;
import java.util.Arrays;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class GGiveGUI extends MenuPagged<GGiveMaterial> {

    public GGiveGUI() {
        super(null, new ArrayList<>(Arrays.asList(GGiveMaterial.values())));
        setSize(9);
        setTitle("&9&lGUI /give");
    }

    @Override
    protected ItemStack convertToItemStack(final GGiveMaterial mat) {
        return ItemUtil.genItem(mat.getMaterial()).name("&7" + mat.getName()).build();
    }

    @Override
    protected void onPageClick(final Player p, final GGiveMaterial mat, final ClickType click) {
        p.closeInventory();
        p.getInventory().addItem(ItemUtil.genItem(mat.getMaterial()).build());
        say(p, "You have received your item(s)");
    }

    @Override
    protected String[] getInfo() {
        return null;
    }
}
