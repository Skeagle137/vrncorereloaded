package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.VRNLib;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.inventorygui.PaginationPanel;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.concurrent.CompletableFuture;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesGUI {

    public HomesGUI(HomeManager manager, Player player, OfflinePlayer target) {
        BorderedGUI gui = new BorderedGUI("Viewing " + target.getName() + "&r's " + "homes");
        PaginationPanel panel = gui.paginate();
        manager.getHomes(target).thenAcceptAsync(homes -> {
            homes.forEach(h -> {
                panel.addPagedButton(ItemButton.create(getIcon(h), e -> {
                    if (e.getClick().isLeftClick()) {
                        player.closeInventory();
                        say(player, BukkitMessages.msg("teleporting"));
                        player.teleport(h.location());
                    }
                    if (e.getClick().isRightClick()) {
                        if (!player.getUniqueId().equals(target.getUniqueId()) && !player.hasPermission("vrn.delhome.others")) {
                            player.closeInventory();
                            say(player, VRNUtil.NOPERM);
                            return;
                        }
                        player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 0.5f);
                        this.deleteConfirm(manager, player, target, h);
                    }
                }));
            });
        }).thenRun(() -> Task.syncDelayed(() -> gui.open(player)));
    }

    private ItemStack getIcon(Home h) {
        Block b = VRNUtil.getStandingBlock(h.location());
        return new ItemBuilder(b != null ? b.getType() : Material.BARRIER).setName("&7" + h.name());
    }

    private void deleteConfirm(HomeManager manager, Player player, OfflinePlayer target, Home h) {
        InventoryGUI gui = new InventoryGUI(9, "&c&lConfirm delete?");
        gui.addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to homes list)"), e -> new HomesGUI(manager, player, target)), 2);
        gui.getInventory().setItem(4, new ItemBuilder(Material.MAP).setName("&6Are you sure?").setLore("", "&eAre you sure you want to", "&edelete this home?"));
        gui.addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
            manager.deleteHome(h);
            player.closeInventory();
            say(player, "&7Home &a" + h.name() + "&7 successfully deleted.");
        }), 6);
        gui.open(player);
    }
}
