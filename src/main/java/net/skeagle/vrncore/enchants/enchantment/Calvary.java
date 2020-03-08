package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Collections;

public class Calvary extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new Calvary();

    private Calvary() {
        super("Calvary", 5);
    }

    @Override
    public String setDescription() {
        return "Provides extra levels of defense for horses.";
    }

    @Override
    public int setRarityPoints() {
        return 200;
    }

    @Override
    public int setRarityFactor() {
        return 100;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.HORSE));
    }
}
