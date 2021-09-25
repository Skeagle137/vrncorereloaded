package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.utils.ItemUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.VRNLib;
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
import java.util.stream.Collectors;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesGUI extends PageableGUI<Home> {

    private final Player target;

    public HomesGUI(Player target) {
        super("Viewing " + target.getDisplayName() + "&r's " + "homes");
        this.target = target;
    }

    @Override
    protected List<Home> getContents() {
        return VRNcore.getInstance().getHomeManager().getHomes().stream().filter(h -> h.owner().equals(target.getUniqueId())).collect(Collectors.toList());
    }

    @Override
    protected ItemStack convertToItem(Home h) {
        Block b = VRNUtil.getStandingBlock(h.location());
        return ItemUtil.genItem(b != null ? b.getType() : Material.BARRIER).name("&7" + h.name()).build();
    }

    @Override
    protected void onClickItem(Home h, InventoryClickEvent e) {
        Player player = getViewer();
        if (e.getClick().isLeftClick()) {
            player.closeInventory();
            say(player, Messages.msg("teleporting"));
            player.teleport(h.location());
        }
        if (e.getClick().isRightClick()) {
            String perm = "vrn.delhome." + (h.owner() != target.getUniqueId() ? "others" : "self");
            if (!player.hasPermission(perm)) {
                player.closeInventory();
                say(player, Messages.getLoaded(VRNLib.getInstance()).get("noPermission"));
                return;
            }
            player.playSound(player.getLocation(), Sound.BLOCK_NOTE_BLOCK_PLING, 5f, 0.5f);
            new DeleteConfirm(player, target, h);
        }
    }

    @Override
    protected ItemButton getInfoItem() {
        return null;
    }

    private static class DeleteConfirm {

        private DeleteConfirm(Player player, Player target, Home h) {
            InventoryGUI gui = new InventoryGUI(9, "&c&lConfirm delete?");
            gui.addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to homes list)"), e -> new HomesGUI(target).open(player)), 2);
            gui.getInventory().setItem(4, new ItemBuilder(Material.MAP).setName("&6Are you sure?").setLore("", "&eAre you sure you want to", "&edelete this home?"));
            gui.addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
                VRNcore.getInstance().getHomeManager().deleteHome(h);
                player.closeInventory();
                say(player, "&7Home &a" + h.name() + "&7 successfully deleted.");
            }), 6);
            gui.open(player);
        }
    }
}
