package net.skeagle.vrncore.GUIs;

import net.minecraft.server.v1_16_R3.NBTTagList;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

import java.util.Collections;

public class InvseeGUI extends MenuPagged<NBTTagList> {

    public InvseeGUI(final Player p, NBTTagList nbt) {
        super(Collections.singleton(nbt));
        setTitle("Viewing " + p.getDisplayName() + "&r's " + "homes");
    }

    @Override
    protected ItemStack convertToItemStack(NBTTagList list) {
        return null;
    }

    @Override
    protected void onPageClick(Player p, NBTTagList item, ClickType click) {
        if (p.getInventory().firstEmpty() == -1) {
            //p.getInventory().setItem(item);
        }
    }
}