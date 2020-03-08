package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.GenericEvent;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.block.BlockBreakEvent;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Collections;

public class GemstoneEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new GemstoneEnchant();

    private GemstoneEnchant() {
        super("Gemstone", 3);
    }

    @Override
    protected void onBreakBlock(final int level, final BlockBreakEvent e) {
        final GenericEvent ge = new GenericEvent();
        if (ge.calcChance(5, level)) {
            e.setExpToDrop(e.getExpToDrop() * 3);
        }
    }

    @Override
    public String setDescription() {
        return "Chance of getting triple the xp from mining an ore. Higher levels increase the chance.";
    }

    @Override
    public int setRarityPoints() {
        return 400;
    }

    @Override
    public int setRarityFactor() {
        return 20;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.PICKAXE));
    }
}
