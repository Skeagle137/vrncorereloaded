package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.api.util.ItemUtil;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.*;
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

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class HomesGUI extends MenuPagged<String> {

    private final Player p;

    public HomesGUI(final Player p) {
        super(HomeManager.getInstance().getHomeNames(p));
        setTitle("Viewing " + p.getDisplayName() + "&r's " + "homes");
        this.p = p;
    }

    @Override
    protected ItemStack convertToItemStack(final String s) {
        return ItemUtil.genItem(getIcon(s)).name("&7" + s).build();
    }

    @Override
    protected void onPageClick(final Player p, final String s, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, "Teleporting...");
            p.teleport(HomeManager.getInstance().getHome(s, p).getLocation());
        }
        if (click.isRightClick()) {
            String perm;
            if (!HomeManager.getInstance().getHome(s, p).getOwner().equals(p.getUniqueId()))
                perm = "vrn.delhome.others";
            else
                perm = "vrn.delhome.self";
            if (!p.hasPermission(perm)) {
                getViewer().closeInventory();
                say(getViewer(), VRNUtil.noperm);
                return;
            }
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new HomesGUI.DeleteConfirm(p, s).displayTo(getViewer());
        }
    }

    @Override
    protected String[] getInfo() {
        return null;
    }

    private final class DeleteConfirm extends Menu {

        private final Button cancel;
        private final Button confirm;
        private final Button info;

        private DeleteConfirm(final Player p, final String s) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new HomesGUI(p), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    HomeManager.getInstance().delHome(s, player);
                    p.closeInventory();
                    say(p, "&7Home &a" + s + "&7 successfully deleted.");
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
                            "&edelete this home?").build();
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

    private Material getIcon(String s) {
        final Block b = VRNUtil.getBlockExact(HomeManager.getInstance().getLocationFromName(p, s));
        return b != null ? b.getType() : Material.BARRIER;
    }
}
