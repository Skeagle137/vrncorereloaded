package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.ItemUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrnlib.commandmanager.Messages;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class WarpsGUI extends MenuPagged<Warp> {

    public WarpsGUI() {
        super(VRNcore.getInstance().getWarpManager().getWarps());
        setTitle("Global warps list");
    }

    @Override
    protected ItemStack convertToItemStack(final Warp w) {
        return ItemUtil.genItem(getIcon(w)).name("&7" + w.getName()).build();
    }

    @Override
    protected void onPageClick(final Player p, final Warp w, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, Messages.msg("teleporting"));
            p.teleport(w.getLocation());
        }
        if (click.isRightClick()) {
            final String perm = "vrn.delwarp." + (w.getOwner() != p.getUniqueId() ? "others" : "self");
            if (!p.hasPermission(perm)) {
                getViewer().closeInventory();
                say(getViewer(), VRNUtil.noperm);
                return;
            }
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new DeleteConfirm(w).displayTo(getViewer());
        }
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    private static final class DeleteConfirm extends Menu {

        private final Button cancel;
        private final Button confirm;
        private final Button info;

        private DeleteConfirm(final Warp w) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new WarpsGUI(), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    VRNcore.getInstance().getWarpManager().deleteWarp(w);
                    getViewer().closeInventory();
                    say(getViewer(), "&7Warp &a" + w.getName() + "&7 successfully deleted.");
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.LIME_WOOL,
                            "&aConfirm").build();
                }
            };
            info = new Button() {

                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemUtil.genItem(Material.MAP,
                            "&6Are you sure?",
                            "",
                            "&eAre you sure you want to",
                            "&edelete this warp?").build();
                }
            };
        }

        @Override
        public ItemStack getItemAt(final int slot) {

            if (slot == 2)
                return cancel.getItem();

            if (slot == 4)
                return info.getItem();

            if (slot == 6)
                return confirm.getItem();

            return null;
        }

        @Override
        protected String[] getInfo() {
            return null;
        }
    }

    private Material getIcon(final Warp w) {
        final Block b = VRNUtil.getStandingBlock(w.getLocation());
        return b != null ? b.getType() : Material.BARRIER;
    }
}
