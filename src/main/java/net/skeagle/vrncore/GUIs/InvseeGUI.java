package net.skeagle.vrncore.GUIs;

import net.minecraft.server.v1_16_R3.NBTTagCompound;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.MenuPagged;

import java.util.List;

public class InvseeGUI extends MenuPagged<NBTTagCompound> {

    public InvseeGUI(List<NBTTagCompound> nbt, OfflinePlayer p) {
        super(nbt);
        setTitle("Viewing " + p.getName() + "&r's " + "inventory");
    }

    @Override
    protected ItemStack convertToItemStack(NBTTagCompound nbt) {
        if (!nbt.isEmpty())
            return CraftItemStack.asBukkitCopy(net.minecraft.server.v1_16_R3.ItemStack.a(nbt));
        return new ItemStack(Material.AIR);
    }

    @Override
    protected void onPageClick(Player p, NBTTagCompound nbtitem, ClickType click) {
        if (p.getInventory().firstEmpty() == -1) {
            animateTitle("&cNo more room in inventory.");
            return;
        }
    }
}