package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.ExpUtil;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.LinkedList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class ExpTradeGUI {

    public ExpTradeGUI(final Player player) {
        final InventoryGUI gui = new InventoryGUI(18, "&9&lExp Trade - Menu");
        for (int i = 0; i < ExpMaterial.values().length; i++) {
            final ExpMaterial expmat = ExpMaterial.values()[i];
            final String name = expmat.toString().toLowerCase().replaceAll("_", " ");
            gui.addButton(ItemButton.create(new ItemBuilder(expmat.mat).setName("&l" + name)
                    .setLore("&9-----------------------------------------",
                            "&7One &b" + name + " &7is worth &b" + expmat.worth + " &7exp level(s).",
                            "&9-----------------------------------------"), e -> {
                if (checkInv(player, expmat.mat)) {
                    new ExpTradeAmount(player, name, expmat);
                } else {
                    player.closeInventory();
                    say(player, "&cThat item is not in your inventory.");
                }
            }), i);
        }
        final int level = player.getLevel();
        final float exp = player.getExp();
        gui.getInventory().setItem(17, new ItemBuilder(Material.MAP).setName("&l&oStats")
                .setLore("&9-----------------------------------------",
                        "&eCurrent Exp level: &6" + level,
                        "&eCurrent next level progress: &6" + String.format("%.0f%%", exp),
                        "&9-----------------------------------------"));
        gui.open(player);
    }

    private boolean checkInv(final Player p, final Material mat) {
        for (final ItemStack item : p.getInventory().getContents()) {
            if (item != null && item.getType() == mat)
                return true;
        }
        return false;
    }

    private static class ExpTradeAmount extends InventoryGUI {

        private int amount;
        private int displayAmount;
        private int stacks;
        private double gained;
        private int finalLevel;

        private ExpTradeAmount(final Player player, final String name, final ExpMaterial expmat) {
            super(9, "&9&lExp Trade - Select Amount");
            this.amount = 1;
            this.displayAmount = 1;
            this.gained = expmat.worth;
            this.finalLevel = (int) (ExpUtil.getExp(player) + gained);

            addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to menu)"), e ->
                    new ExpTradeGUI(player)), 2);


            addButton(ItemButton.create(new ItemBuilder(expmat.mat).setName("&l" + name).setCount(displayAmount)
                    .setLore(getLore()), (e, button) -> {
                if (1 > amount - 1 && e.isRightClick() || getAmount(player, expmat.mat) < amount + 1 && e.isLeftClick())
                    return;
                amount = e.isShiftClick() ? (e.isLeftClick() ? amount + 10 : amount - 10) : (e.isLeftClick() ? amount + 1 : amount - 1);
                stacks = amount / 64;
                displayAmount = amount % 64 != 0 ? amount % 64 : 1;
                System.out.println(ExpUtil.getExp(player) + " " + player.getExp() + " " + getExpToNext(player.getLevel()));
                System.out.println(expmat.worth + " " + expmat.worth * amount);
                System.out.println(ExpUtil.getExpToNext(Math.round(player.getLevel())));
                System.out.println((ExpUtil.getExp(player) - ExpUtil.getExpFromLevel(player.getLevel())));
                gained = expmat.worth * amount;
                finalLevel = (int) Math.round((ExpUtil.getExp(player) + gained));
                button.setItem(new ItemBuilder(button.getItem()).setCount(displayAmount).setLore(getLore()));
                update();
            }), 4);

            addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
                for (final ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.getType() == expmat.mat) {
                        if (item.getAmount() >= amount) {
                            player.getInventory().removeItem(new ItemStack(item.getType(), amount));
                            ExpUtil.changeExp(player, (int) (gained));
                            say(player, "You traded &ax" + amount + " " + name + " &7for &a" + gained + " &7exp level(s).");
                            break;
                        }
                    }
                }
                player.closeInventory();
            }), 6);

            open(player);
        }

        private int getAmount(final Player p, final Material mat) {
            int total = 0;
            for (final ItemStack item : p.getInventory().getContents()) {
                if (item != null && item.getType() == mat)
                    total += item.getAmount();
            }
            return total;
        }

        private String[] getLore() {
            return new String[] {"&9-----------------------------------------",
            "&fLeft or Right click &7increases/decreases the amount by &b1&7.",
                    "&fShift Left or Right click &7increases/decreases the amount by &b10&7.",
                    "&7Current amount to trade: &d" + (stacks != 0 ? stacks + " &7stack(s)" : "") + (amount % 64 != 0 && stacks != 0 ? " and " : "")
                            + "&b" + (amount % 64 != 0 ? displayAmount : "") + (stacks != 0 ? " &7(&e" + amount + "&7 total)" : ""),
                    "&7Exp gain from trade: &b" + gained + " level(s)",
                    "&7Final Exp level after trade: &a" + finalLevel,
                    "&9-----------------------------------------"};
        }

        private int getExpToNext(final int level) {
            if (level >= 30) {
                return 112 + (level - 30) * 9;
            } else {
                return level >= 15 ? 37 + (level - 15) * 5 : 7 + level * 2;
            }
        }
    }

    private enum ExpMaterial {
        NETHERITE(1.5, Material.NETHERITE_INGOT), // 1 and 1/2
        ANCIENT_DEBRIS(1.0, Material.ANCIENT_DEBRIS), // 1
        DIAMOND(0.25, Material.DIAMOND), // 1/4
        EMERALD(0.1, Material.EMERALD), // 1/10
        GOLD_INGOT(0.03125, Material.GOLD_INGOT), // 1/32
        IRON_INGOT(0.0208, Material.IRON_INGOT), // 1/48
        NETHER_QUARTZ(0.015, Material.QUARTZ), // 1/64
        LAPIS_LAZULI(0.01, Material.LAPIS_LAZULI), // 1/100
        COAL(0.01, Material.COAL), // 1/100
        REDSTONE_DUST(0.01, Material.REDSTONE); // 1/100

        private final double worth;
        private final Material mat;

        ExpMaterial(final double worth, final Material mat) {
            this.worth = worth;
            this.mat = mat;
        }
    }
}
