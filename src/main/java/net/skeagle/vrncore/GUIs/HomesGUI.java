package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.utils.ItemUtil;
import net.skeagle.vrncore.utils.VRNUtil;
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

import java.util.stream.Collectors;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesGUI extends MenuPagged<Home> {

    private final Player target;

    public HomesGUI(final Player target) {
        super(VRNcore.getInstance().getHomeManager().getHomes().stream().filter(h -> h.getOwner() == target.getUniqueId()).collect(Collectors.toList()));
        setTitle("Viewing " + target.getDisplayName() + "&r's " + "homes");
        this.target = target;
    }

    @Override
    protected ItemStack convertToItemStack(final Home h) {
        return ItemUtil.genItem(getIcon(h)).name("&7" + h.getName()).build();
    }

    @Override
    protected void onPageClick(final Player p, final Home h, final ClickType click) {
        if (click.isLeftClick()) {
            p.closeInventory();
            say(p, Messages.msg("teleporting"));
            p.teleport(h.getLocation());
        }
        if (click.isRightClick()) {
            final String perm = "vrn.delhome." + (h.getOwner() != target.getUniqueId() ? "others" : "self");
            if (!p.hasPermission(perm)) {
                p.closeInventory();
                say(p, VRNUtil.noperm);
                return;
            }
            CompSound.NOTE_PLING.play(p.getLocation(), 5f, 0.5f);
            new HomesGUI.DeleteConfirm(target, h).displayTo(p);
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

        private DeleteConfirm(final Player target, final Home h) {
            super(null);
            setSize(9);
            setTitle("&c&lConfirm delete?");

            cancel = new ButtonMenu(new HomesGUI(target), CompMaterial.RED_WOOL, "&4Cancel");
            confirm = new Button() {
                @Override
                public void onClickedInMenu(final Player p, final Menu menu, final ClickType clickType) {
                    VRNcore.getInstance().getHomeManager().deleteHome(h);
                    p.closeInventory();
                    say(p, "&7Home &a" + h.getName() + "&7 successfully deleted.");
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

    private Material getIcon(final Home h) {
        final Block b = VRNUtil.getStandingBlock(h.getLocation());
        return b != null ? b.getType() : Material.BARRIER;
    }
}
