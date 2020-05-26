package net.skeagle.vrncore.GUIs.exptrade;

import net.skeagle.vrncore.GUIs.CustomInventory;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static jdk.nashorn.internal.objects.NativeMath.round;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class ExpTradeGUI extends CustomInventory {


    public ExpTradeGUI(final Player p) {
        super(18, "&9&lExp Trade - Menu");
        int slot = 0;

        for (final ExpMaterial expmat : ExpMaterial.values()) {
            final String name = expmat.toString().toLowerCase().replaceAll("_", " ");
            final Material icon = expmat.getIcon();
            final double worth = expmat.getValue();
            final ItemStack i = new ItemStack(icon, 1);

            setItem(slot, i, player ->
            {
                if (checkInv(p, i)) {
                    new ExpTradeAmount(p, name, icon, worth).open(player);
                } else {
                    player.closeInventory();
                    say(p, "&cThat item is not in your inventory.");
                }
            }, "&l" + name, new String[]{
                    "&9-----------------------------------------",
                    "&7One &b" + name + " &7is worth &b" + worth + " &7exp level(s).",
                    "&9-----------------------------------------"});
            slot++;
        }
        final int exp = p.getLevel();
        final float points = p.getExp();
        setItem(17, new ItemStack(Material.MAP), player ->
        {
        }, "&l&oStats", new String[]{
                "&9-----------------------------------------",
                "&eCurrent Exp level: &6" + exp,
                "&eCurrent next level progress: &6" + round(points, 2) + "%",
                "&9-----------------------------------------"});
    }
    private boolean checkInv(final Player player, final ItemStack i) {
        for(final ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == i.getType()) {
                return true;
            }
        }
        return false;
    }
}
