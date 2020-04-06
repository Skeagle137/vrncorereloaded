package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.menu.CustomInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static net.skeagle.vrncore.utils.VRNUtil.say;

class ExpTradeAmount extends CustomInventory {

    ExpTradeAmount(final Player p, final String name, final Material icon, final Double worth) {
        super(9, "Exp Trade - Choose Amount");
        final ItemStack i = new ItemStack(Material.AIR);
        final int amount = i.getAmount();
        i.setType(icon);
        i.setAmount(amount);


        setItem(0, new ItemStack(Material.EMERALD_BLOCK), player ->
            new ExpTradeConfirm(p, name, icon, worth, 1).open(player), "&aConfirm item amount" , new String[]{});

        setItem(2, new ItemStack(Material.MAP), player ->
        {
            final int newAmount = i.getAmount() - 10;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3-10" , new String[]{"&7Click to decrease item amount by &b10"});

        setItem(3, new ItemStack(Material.PAPER), player ->
        {
            final int newAmount = i.getAmount() - 1;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3-1", new String[]{"&7Click to decrease item amount by &b1"});

        setItem(4, i, player ->
                {}, "&l" + name, new String[]{
                "&9-----------------------------------------",
                "&7Current amount to trade: &b" + i.getAmount(),
                "&9-----------------------------------------"});

        setItem(5, new ItemStack(Material.PAPER), player ->
        {
            final int newAmount = 1 + i.getAmount();
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3+1", new String[]{"&7Click to increase item amount by &b1"});

        setItem(6, new ItemStack(Material.MAP), player ->
        {
            final int newAmount = 10 + i.getAmount();
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3+10", new String[]{"&7Click to increase item amount by &b10"});

        setItem(8, new ItemStack(Material.ARROW), player ->
                new ExpTradeGUI(p).open(player), "&7&lBack", new String[]{});
    }

    private ExpTradeAmount(final Player p, final String name, final Material icon, final Double worth, int amountnew) {
        super(9, "Exp Trade - Choose Amount");
        final ItemStack i = new ItemStack(Material.AIR);
        i.setType(icon);
        if (amountnew < 1) {
            amountnew = 1;
        }
        else if (amountnew > 64) {
            amountnew = 64;
        }
        i.setAmount(amountnew);
        final int finalAmountnew = amountnew;


        setItem(0, new ItemStack(Material.EMERALD_BLOCK), player ->
        {

            for(final ItemStack item : p.getInventory().getContents()) {
                if (item != null && item.getType() == i.getType()) {
                    if (item.getAmount() >= finalAmountnew) {
                        new ExpTradeConfirm(p, name, icon, worth, finalAmountnew).open(player);
                    }
                    else {
                        player.closeInventory();
                        say(p, "&cTrade canceled, the specified amount was more than the amount of the item you attempted to trade.");
                    }
                }
            }
        }, "&aConfirm item amount" , new String[]{});

        setItem(2, new ItemStack(Material.MAP), player ->
        {
            final int newAmount = finalAmountnew - 10;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3-10" , new String[]{"&7Click to decrease item amount by &b10"});

        setItem(3, new ItemStack(Material.PAPER), player ->
        {
            final int newAmount = finalAmountnew - 1;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3-1", new String[]{"&7Click to decrease item amount by &b1"});

        setItem(4, i, player ->
        {}, "&l" + name, new String[]{
                "&9-----------------------------------------",
                "&7Current amount to trade: &b" + i.getAmount(),
                "&9-----------------------------------------"});

        setItem(5, new ItemStack(Material.PAPER), player ->
        {
            final int newAmount = 1 + finalAmountnew;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3+1", new String[]{"&7Click to increase item amount by &b1"});

        setItem(6, new ItemStack(Material.MAP), player ->
        {
            final int newAmount = 10 + finalAmountnew;
            i.setAmount(newAmount);
            new ExpTradeAmount(p, name, icon, worth, newAmount).open(player);
        }, "&3+10", new String[]{"&7Click to increase item amount by &b10"});

        setItem(8, new ItemStack(Material.ARROW), player ->
                new ExpTradeGUI(p).open(player), "&7&lBack", new String[]{});
    }
}
