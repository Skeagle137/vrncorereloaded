package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.ExpUtil;
import net.skeagle.vrnlib.inventorygui.InventoryGUI;
import net.skeagle.vrnlib.inventorygui.ItemButton;
import net.skeagle.vrnlib.itemutils.ItemBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

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
        private int stacks;
        private double gained;
        private int finalLevel;

        private ExpTradeAmount(final Player player, final String name, final ExpMaterial expmat) {
            this(player, name, expmat, 1, 0);
        }

        private ExpTradeAmount(final Player player, final String name, final ExpMaterial expmat, final int initAmount, final int initStacks) {
            super(9, "&9&lExp Trade - Select Amount");
            this.amount = initAmount;
            this.stacks = initStacks;

            gained = expmat.worth;
            finalLevel = (int) (ExpUtil.getExp(player) + gained);

            addButton(ItemButton.create(new ItemBuilder(Material.RED_WOOL).setName("&cCancel (Back to menu)"), e ->
                    new ExpTradeGUI(player)), 2);


            addButton(new ItemButton() {
                @Override
                public ItemStack getItem() {
                    return new ItemBuilder(expmat.mat).setName("&l" + name)
                            .setLore("&9-----------------------------------------",
                                    "&fLeft or Right click &7increases/decreases the amount by &b1&7.",
                                    "&fShift Left or Right click &7increases/decreases the amount by &b10&7.",
                                    "&7Current amount to trade: &d" + (stacks != 0 ? stacks + " &7stack(s)" : "") + (amount != 0 && stacks != 0 ? " and " : "")
                                            + "&b" + (amount != 0 ? amount : "") + (stacks != 0 ? " &7(&e" + ((stacks * 64) + amount) + "&7 total)" : ""),
                                    "&7Exp gain from trade: &b" + gained + " level(s)",
                                    "&7Final Exp level after trade: &a" + finalLevel,
                                    "&9-----------------------------------------");
                }

                @Override
                public void onClick(final InventoryClickEvent e) {
                    final int prevAmount = amount;
                    final int prevStacks = stacks;
                    if (!e.isShiftClick() && e.isLeftClick())
                        amount++;
                    if (!e.isShiftClick() && e.isRightClick())
                        amount--;
                    if (e.isShiftClick() && e.isLeftClick())
                        amount += 10;
                    if (e.isShiftClick() && e.isRightClick())
                        amount -= 10;
                    if (e.isLeftClick()) {
                        if (stacks == 36 && amount > 0)
                            amount = 0;
                        if (amount > 63) {
                            amount -= 64;
                            stacks++;
                        }
                    }
                    if (e.isRightClick()) {
                        if (amount < 1 && stacks == 0)
                            amount = 1;
                        if (amount < 0) {
                            amount += 64;
                            stacks--;
                        }
                    }
                    if (getAmount(player, expmat.mat) < (stacks * 64) + amount) {
                        amount = prevAmount;
                        stacks = prevStacks;
                        //animate "No more items to trade."
                    }
                    System.out.println(expmat.worth + " " + expmat.worth * amount);
                    System.out.println(ExpUtil.getExpToNext(Math.round(player.getLevel() + player.getExp())));
                    gained = expmat.worth * ((stacks * 64) + amount);
                    finalLevel = (int) Math.round((ExpUtil.getExp(player) + gained));
                    update();
                }
            }, 4);

            addButton(ItemButton.create(new ItemBuilder(Material.LIME_WOOL).setName("&aConfirm"), e -> {
                player.closeInventory();
                final int trueAmount = (this.stacks * 64) + this.amount;
                for (final ItemStack item : player.getInventory().getContents()) {
                    if (item != null && item.getType() == expmat.mat) {
                        if (item.getAmount() >= trueAmount) {
                            player.getInventory().removeItem(new ItemStack(item.getType(), trueAmount));
                            ExpUtil.changeExp(player, (int) (gained));
                            say(player, "You traded &ax" + trueAmount + " " + name + " &7for &a" + gained + " &7exp level(s).");
                            break;
                        }
                    }
                }
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
