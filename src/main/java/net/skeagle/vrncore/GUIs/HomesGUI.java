package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.storage.homes.HomesManager;
import net.skeagle.vrncore.utils.storage.homes.HomesResource;
import net.skeagle.vrncore.utils.storage.homes.RegisteredHome;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.menu.Menu;
import org.mineacademy.fo.menu.MenuPagged;
import org.mineacademy.fo.menu.button.Button;
import org.mineacademy.fo.menu.button.ButtonMenu;
import org.mineacademy.fo.menu.model.ItemCreator;
import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompSound;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesGUI extends MenuPagged<RegisteredHome> {

    public HomesGUI(final Player p) {
        super(null, HomesResource.getInstance().getHome(p.getUniqueId()).getHomes());
        setSize(9 * 4);
        setTitle("Viewing " + (p == getViewer() ? "" : p.getDisplayName() + "&r's ") + "homes");
    }

    @Override
    protected ItemStack convertToItemStack(final RegisteredHome home) {
        return ItemCreator.of(home.getIcon()).name("&7" + home.getName()).build().make();
    }

    @Override
    protected void onPageClick(final Player p, final RegisteredHome home, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, "Teleporting...");
            p.teleport(home.getLoc());
        }
        if (click.isRightClick()) {
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new HomesGUI.DeleteConfirm(p, home).displayTo(getViewer());
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

        private DeleteConfirm(final Player p, final RegisteredHome home) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new HomesGUI(p), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player player, final Menu menu, final ClickType clickType) {
                    final HomesManager man = HomesResource.getInstance().getHome(p.getUniqueId());
                    man.delHome(home);
                    p.closeInventory();
                    say(p, "&7Home &a" + home.getName() + "&7 successfully deleted.");
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
}
