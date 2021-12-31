package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.utils.VRNUtil;
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

public class HomesGUI {

    public HomesGUI(Player player, Player target) {
        BorderedGUI gui = new BorderedGUI("Viewing " + target.getDisplayName() + "&r's " + "homes");
        PaginationPanel panel = gui.paginate();
        VRNcore.getInstance().getHomeManager().getHomes().stream().filter(h -> h.owner().equals(target.getUniqueId())).forEach(h ->
                panel.addPagedButton(ItemButton.create(getIcon(h), e -> {
                    if (e.getClick().isLeftClick()) {
                        player.closeInventory();
                        say(player, BukkitMessages.msg("teleporting"));
                        player.teleport(h.location());
                    }
                    if (e.getClick().isRightClick() && player != target) {
                        if (!player.hasPermission("vrn.delhome.others")) {
                            player.closeInventory();
                            say(player, BukkitMessages.msg("noPermission"));
                            return;
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 0.5f);
                        deleteConfirm(player, target, h);
                    }
                }))
        );
        gui.open(player);
    }

    private ItemStack getIcon(Home h) {
        Block b = VRNUtil.getStandingBlock(h.location());
        return new ItemBuilder(b != null ? b.getType() : Material.BARRIER).setName("&7" + h.name());
    }

    private void deleteConfirm(Player player, Player target, Home h) {
        InventoryGUI gui = new InventoryGUI(9, "&c&lConfirm delete?");
        gui.addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to homes list)"), e -> new HomesGUI(player, target)), 2);
        gui.getInventory().setItem(4, new ItemBuilder(Material.MAP).setName("&6Are you sure?").setLore("", "&eAre you sure you want to", "&edelete this home?"));
        gui.addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
            VRNcore.getInstance().getHomeManager().deleteHome(h);
            player.closeInventory();
            say(player, "&7Home &a" + h.name() + "&7 successfully deleted.");
        }), 6);
        gui.open(player);
    }
}
