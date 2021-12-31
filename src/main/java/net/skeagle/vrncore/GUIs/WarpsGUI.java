package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.inventorygui.PaginationPanel;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class WarpsGUI {

    public WarpsGUI(Player player) {
        BorderedGUI gui = new BorderedGUI("Global warps list");
        PaginationPanel panel = gui.paginate();
        VRNcore.getInstance().getWarpManager().getWarps().forEach(w ->
                panel.addPagedButton(ItemButton.create(getIcon(w), e -> {
                    if (e.getClick().isLeftClick()) {
                        player.closeInventory();
                        say(player, BukkitMessages.msg("teleporting"));
                        player.teleport(w.location());
                    }
                    if (e.getClick().isRightClick() && !w.owner().equals(player.getUniqueId())) {
                        if (!player.hasPermission("vrn.delwarp.others")) {
                            player.closeInventory();
                            say(player, BukkitMessages.msg("noPermission"));
                            return;
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 0.5f);
                        deleteConfirm(player, w);
                    }
                }))
        );
        gui.open(player);
    }

    private ItemStack getIcon(Warp w) {
        Block b = VRNUtil.getStandingBlock(w.location());
        return new ItemBuilder(b != null ? b.getType() : Material.BARRIER).setName("&7" + w.name());
    }

    private void deleteConfirm(Player player, Warp w) {
        InventoryGUI gui = new InventoryGUI(9, "&c&lConfirm delete?");
        gui.addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to homes list)"), e -> new WarpsGUI(player)), 2);
        gui.getInventory().setItem(4, new ItemBuilder(Material.MAP).setName("&6Are you sure?").setLore("", "&eAre you sure you want to", "&edelete this home?"));
        gui.addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
            VRNcore.getInstance().getWarpManager().deleteWarp(w);
            player.closeInventory();
            say(player, "&7Warp &a" + w.name() + "&7 successfully deleted.");
        }), 6);
        gui.open(player);
    }
}
