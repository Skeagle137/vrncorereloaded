package net.skeagle.vrncore.GUIs.exptrade;

import net.skeagle.vrncore.GUIs.CustomInventory;
import net.skeagle.vrncore.utils.menu.ExpUtil;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static jdk.nashorn.internal.objects.NativeMath.round;
import static net.skeagle.vrncore.utils.VRNUtil.say;

class ExpTradeConfirm extends CustomInventory {

    ExpTradeConfirm(final Player p, final String name, final Material icon, final Double worth, final int amount) {
        super(9, "&9&lExp Trade - Confirm");

        final float calcCost = (float) (ExpUtil.getExp(p) + worth * amount);
        final int finalLevel = (int) ExpUtil.getLevelFromExp((long) calcCost);
        setItem(0, new ItemStack(Material.MAP), player -> {
        }, "&9&lInformation", new String[]{
                "&9Material: &a" + name,
                "&9Amount of material to trade: &a" + amount,
                "&9Exp gain from trade: &a" + round((worth * amount), 2) + " level(s)",
                "&9Final Exp level after trade: &a" + finalLevel});
        final ItemStack i = new ItemStack(Material.AIR);
        i.setType(icon);
        i.setAmount(amount);

        setItem(3, new ItemStack(Material.EMERALD_BLOCK), player -> {
            player.closeInventory();
            for (final ItemStack item : p.getInventory().getContents()) {
                if (item != null && item.getType() == i.getType()) {
                    if (item.getAmount() >= amount) {
                        p.getInventory().removeItem(new ItemStack(item.getType(), amount));
                        expCalc(p, worth, amount);
                        say(player, "&dYou traded &5" + amount + "x &3" + name + " &dfor &2" + (worth * amount) + " &dexp level(s).");
                        break;
                    }
                }
            }
        }, "&2&lCONFIRM", new String[]{});


        setItem(5, new ItemStack(Material.REDSTONE_BLOCK), player -> {
            say(player, "&cTrade canceled.");
            player.closeInventory();
        }, "&4&lCANCEL", new String[]{});


        setItem(8, new ItemStack(Material.ARROW), player ->
                new ExpTradeAmount(p, name, icon, worth).open(player), "&7&lBack", new String[]{});
    }

    private void expCalc(final Player p, final double worth, final int amount) {
        float exp = 0;
        exp += ExpUtil.getExp(p); //14

        if (exp < 0) {
            exp = 0;
        }

        final double totalexp = ExpUtil.getLevelFromExp(exp) + worth * amount;
        final int finallvl = (int) totalexp;
        p.setLevel(finallvl);
        p.setExp((float) totalexp - finallvl);
    }
}
