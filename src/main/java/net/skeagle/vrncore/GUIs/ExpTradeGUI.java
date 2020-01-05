package net.skeagle.vrncore.GUIs;

import net.skeagle.vrncore.utils.CustomInventory;
import net.skeagle.vrncore.utils.ExpMaterial;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static jdk.nashorn.internal.objects.NativeMath.round;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class ExpTradeGUI extends CustomInventory {


    public ExpTradeGUI(Player p) {
        super(18, "&9&lExp Trade - Menu");
        int slot = 0;

        for (ExpMaterial expmat : ExpMaterial.values()) {
            String name = expmat.toString().toLowerCase().replaceAll("_", " ");
            Material icon = expmat.getIcon();
            double worth = expmat.getValue();
            ItemStack i = new ItemStack(icon, 1);

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
        int exp = p.getLevel();
        float points = p.getExp();
        setItem(17, new ItemStack(Material.MAP), player ->
        {
        }, "&l&oStats", new String[]{
                "&9-----------------------------------------",
                "&eCurrent Exp level: &6" + exp,
                "&eCurrent next level progress: &6" + round(points, 2) + "%",
                "&9-----------------------------------------"});
    }
    private boolean checkInv(Player player, ItemStack i) {
        for(ItemStack item : player.getInventory().getContents()) {
            if (item != null && item.getType() == i.getType()) {
                return true;
            }
        }
        return false;
    }
}
