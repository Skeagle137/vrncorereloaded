package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Arrays;

public class GUIgive implements Listener {
    void give(Player p) {
        Inventory inv = Bukkit.createInventory(null, 9, ChatColor.GOLD + "GUI /give");

        ItemStack instructions = new ItemStack(Material.PAPER);
        ItemMeta inst = instructions.getItemMeta();
        inst.setLore(Arrays.asList(ChatColor.AQUA + "For one item, left click item.", ChatColor.AQUA + "For 64 items, right click item."));
        inst.setDisplayName(ChatColor.DARK_AQUA + "" + ChatColor.BOLD + "Instructions");
        instructions.setItemMeta(inst);

        ItemStack barrier = new ItemStack(Material.BARRIER);
        ItemMeta bar = barrier.getItemMeta();
        bar.setDisplayName(ChatColor.GRAY + "Barrier");
        barrier.setItemMeta(bar);

        ItemStack cmdblock = new ItemStack(Material.COMMAND_BLOCK);
        ItemMeta cmd = cmdblock.getItemMeta();
        cmd.setDisplayName(ChatColor.GRAY + "Command Block");
        cmdblock.setItemMeta(cmd);

        ItemStack cmdblockr = new ItemStack(Material.REPEATING_COMMAND_BLOCK);
        ItemMeta cmdr = cmdblockr.getItemMeta();
        cmdr.setDisplayName(ChatColor.GRAY + "Repeating Command Block");
        cmdblockr.setItemMeta(cmdr);

        ItemStack cmdblockc = new ItemStack(Material.CHAIN_COMMAND_BLOCK);
        ItemMeta cmdc = cmdblockc.getItemMeta();
        cmdc.setDisplayName(ChatColor.GRAY + "Chain Command Block");
        cmdblockc.setItemMeta(cmdc);

        ItemStack mobspawn = new ItemStack(Material.SPAWNER);
        ItemMeta mob = mobspawn.getItemMeta();
        mob.setDisplayName(ChatColor.GRAY + "Mob Spawner");
        mobspawn.setItemMeta(mob);

        ItemStack sblock = new ItemStack(Material.STRUCTURE_BLOCK);
        ItemMeta sb = sblock.getItemMeta();
        sb.setDisplayName(ChatColor.GRAY + "Structure Block");
        sblock.setItemMeta(sb);

        ItemStack svoid = new ItemStack(Material.STRUCTURE_VOID);
        ItemMeta sv = barrier.getItemMeta();
        sv.setDisplayName(ChatColor.GRAY + "Structure Void");
        svoid.setItemMeta(sv);

        ItemStack debugs = new ItemStack(Material.DEBUG_STICK);
        ItemMeta dstick = debugs.getItemMeta();
        dstick.setDisplayName(ChatColor.GRAY + "Debug Stick");
        debugs.setItemMeta(dstick);

        inv.setItem(0, instructions);
        inv.setItem(1, barrier);
        inv.setItem(2, cmdblock);
        inv.setItem(3, cmdblockr);
        inv.setItem(4, cmdblockc);
        inv.setItem(5, mobspawn);
        inv.setItem(6, sblock);
        inv.setItem(7, svoid);
        inv.setItem(8, debugs);

        p.openInventory(inv);
    }

    @EventHandler
    public void Invclick(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        Inventory click = e.getClickedInventory();
        ItemStack i = e.getCurrentItem();

        if (click == null) {
            return;
        }
        if (i == null || !i.hasItemMeta()) {
            return;
        }

        if (e.getView().getTitle().equals(ChatColor.GOLD + "GUI /give")) {
            e.setCancelled(true);
        }
        if (e.getView().getTitle().equals(ChatColor.GOLD + "GUI /give") && e.isShiftClick()) {
            e.setCancelled(true);
        }

        if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Barrier")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.BARRIER, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.BARRIER, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Command Block")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.COMMAND_BLOCK, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.COMMAND_BLOCK, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Repeating Command Block")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.REPEATING_COMMAND_BLOCK, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.REPEATING_COMMAND_BLOCK, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Chain Command Block")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.CHAIN_COMMAND_BLOCK, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.CHAIN_COMMAND_BLOCK, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Mob Spawner")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.SPAWNER, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.SPAWNER, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Structure Block")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.STRUCTURE_BLOCK, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.STRUCTURE_BLOCK, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Structure Void")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.STRUCTURE_VOID, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.STRUCTURE_VOID, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        } else if (i.getItemMeta().getDisplayName().equals(ChatColor.GRAY + "Debug Stick")) {
            if (e.getClick() == ClickType.LEFT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.DEBUG_STICK, 1));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            } else if (e.getClick() == ClickType.RIGHT) {
                p.closeInventory();
                p.getInventory().addItem(new ItemStack(Material.DEBUG_STICK, 64));
                p.sendMessage(VRNcore.vrn + "You have received your item(s)");
            }
        }
    }
}