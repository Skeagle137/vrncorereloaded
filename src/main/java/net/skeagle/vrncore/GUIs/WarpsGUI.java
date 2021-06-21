package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.inventorygui.PageableGUI;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class WarpsGUI extends PageableGUI<Warp> {

    public WarpsGUI() {
        super("Global warps list");
    }

    @Override
    protected List<Warp> getContents() {
        return VRNcore.getInstance().getWarpManager().getWarps();
    }

    @Override
    protected ItemStack convertToItem(Warp w) {
        Block b = VRNUtil.getStandingBlock(w.location());
        return new ItemBuilder(b != null ? b.getType() : Material.BARRIER).setName("&7" + w.name());
    }

    @Override
    protected void onClickItem(Warp w, InventoryClickEvent e) {
        Player player = getViewer();
        if (e.getClick().isLeftClick()) {
            player.closeInventory();
            say(player, Messages.msg("teleporting"));
            player.teleport(w.location());
        }
        if (e.getClick().isRightClick()) {
            String perm = "vrn.delwarp." + (!w.owner().equals(player.getUniqueId()) ? "others" : "self");
            if (!player.hasPermission(perm)) {
                getViewer().closeInventory();
                say(getViewer(), VRNUtil.noperm);
                return;
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 0.5f);
            new DeleteConfirm(player, w);
        }
    }

    @Override
    protected ItemButton getInfoItem() {
        return null;
    }

    private static class DeleteConfirm {

        private DeleteConfirm(Player player, Warp w) {
            InventoryGUI gui = new InventoryGUI(9, "&c&lConfirm delete?");
            gui.addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to homes list)"), e -> new WarpsGUI().open(player)), 2);
            gui.getInventory().setItem(4, new ItemBuilder(Material.MAP).setName("&6Are you sure?").setLore("", "&eAre you sure you want to", "&edelete this home?"));
            gui.addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
                VRNcore.getInstance().getWarpManager().deleteWarp(w);
                player.closeInventory();
                say(player, "&7Warp &a" + w.name() + "&7 successfully deleted.");
            }), 6);
            gui.open(player);
        }
    }
}
