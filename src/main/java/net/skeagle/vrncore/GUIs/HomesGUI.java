package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.*;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesGUI extends MenuPagged<Home> {

    public HomesGUI(final Player p) {
        super(HomeManager.getInstance().getHomes(p));
        setTitle("Viewing " + p.getDisplayName() + "&r's " + "homes");
    }

    @Override
    protected ItemStack convertToItemStack(final Home h) {
        ItemStack i = new ItemStack(getIcon(h), 1);
        ItemMeta meta = i.getItemMeta();
        meta.setDisplayName(VRNUtil.color("&7" + h.getName()));
        i.setItemMeta(meta);
        return i;
    }

    @Override
    protected void onPageClick(final Player p, final Home h, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, "Teleporting...");
            p.teleport(h.getLocation());
        }
        if (click.isRightClick()) {
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new HomesGUI.DeleteConfirm(p, h).displayTo(getViewer());
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

        private DeleteConfirm(final Player p, final Home h) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new HomesGUI(p), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    HomeManager.getInstance().delHome(h.getName(), player);
                    p.closeInventory();
                    say(p, "&7Home &a" + h.getName() + "&7 successfully deleted.");
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.LIME_WOOL,
                            "&aConfirm").build().make();
                }
            };
            info = new Button() {

                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                }

                @Override
                public ItemStack getItem() {
                    return ItemCreator.of(CompMaterial.MAP,
                            "&6Are you sure?",
                            "",
                            "&eAre you sure you want to",
                            "&edelete this home?").build().make();
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

    private Material getIcon(final Home h) {
        final Block b = VRNUtil.getBlockExact(h.getLocation());
        return b != null ? b.getType() : Material.BARRIER;
    }
}
