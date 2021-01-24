package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_16_R3.ItemStack;
import net.minecraft.server.v1_16_R3.NBTCompressedStreamTools;
import net.minecraft.server.v1_16_R3.NBTTagCompound;
import net.minecraft.server.v1_16_R3.NBTTagList;
import net.skeagle.vrncore.GUIs.InvseeGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.craftbukkit.v1_16_R3.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.util.Consumer;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class Invsee extends SimpleCommand {

    public Invsee() {
        super("invsee");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("View another player's inventory.");
        setPermission("vrn.invsee");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override @SuppressWarnings("deprecation")
    protected void onCommand() {
        final Player a = findPlayerInternal(args[0]);
        if (a != null) {
            getPlayer().openInventory(a.getInventory());
            say(getPlayer(), "Now showing &a" + a.getName() + "&7's inventory.");
        }
        say(getPlayer(), VRNUtil.noton);
        /*
        OfflinePlayer p;
        File f;
        if (a.hasPlayedBefore()) {
            p = Bukkit.getOfflinePlayer(args[0]);
            f = new File("world" + File.separator + "playerdata", p.getUniqueId().toString() + ".dat");
        }
        else {
            say(getPlayer(), "&cno player data found for " + args[0] + ".");
            return;
        }
        try {
            NBTTagCompound nbt = NBTCompressedStreamTools.a(new FileInputStream(f));
            new InvseeGUI(getPlayer(), (NBTTagList) nbt.get("Inventory"));
            /*for (int i = 0; i < inventory.size() - 1; i++) {
                NBTTagCompound compound = (NBTTagCompound) inventory.get(i);
                if (!compound.isEmpty()) {
                    org.bukkit.inventory.ItemStack stack = CraftItemStack.asBukkitCopy(ItemStack.a(compound));
                    inv.setItem(i, CraftItemStack.asBukkitCopy(ItemStack.a(compound)));
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
         */
    }
}
