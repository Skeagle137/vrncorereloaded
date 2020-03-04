package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.CompMaterial;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ThreadLocalRandom;

public class AutoSmeltEnchant extends SimpleEnchantment implements IVRNEnchant {
    @Getter
    private static final Enchantment instance = new AutoSmeltEnchant();

    private AutoSmeltEnchant() {
        super("Auto Smelt", 1);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        CompMaterial mat;
        final ItemStack item = e.getPlayer().getEquipment().getItemInMainHand();
        int fortune = 0;
        if (item.getEnchantments().containsKey(Enchantment.LOOT_BONUS_BLOCKS)) {
            fortune = calcFortune(item.getEnchantmentLevel(Enchantment.LOOT_BONUS_BLOCKS));

        }
        if (e.getBlock().getType() == CompMaterial.GOLD_ORE.toMaterial()) {
            mat = CompMaterial.GOLD_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), mat.toItem(fortune));
        }
        if (e.getBlock().getType() == CompMaterial.IRON_ORE.toMaterial()) {
            mat = CompMaterial.IRON_INGOT;
            e.setDropItems(false);
            e.getBlock().getWorld().dropItemNaturally(e.getBlock().getLocation(), mat.toItem(fortune));
        }

    }

    private int calcFortune(final int level) {
        final int min = 1;
        final int max = level + 1;
        if (Math.random() > (float) (100 / (max + 1))) return min;
        return ThreadLocalRandom.current().nextInt(max - min + 1) + min;
    }

    @Override
    public String setDescription() {
        return "Automatically smelts ores.";
    }

    @Override
    public int setRarityPoints() {
        return 150;
    }

    @Override
    public int setRarityFactor() {
        return 0;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.PICKAXE));
    }
}
