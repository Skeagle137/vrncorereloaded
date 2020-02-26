package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Arrays;

public class VenomEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new VenomEnchant();

    private VenomEnchant() {
        super("Venom", 5);
    }

    @Override
    public String setDescription() {
        return "Inflicts the target with poison. The greater the enchant's power, the longer the effect lasts.";
    }

    @Override
    public int setRarityPoints() {
        return 15;
    }

    @Override
    public int setRarityFactor() {
        return 5;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Arrays.asList(ApplyToItem.BOW, ApplyToItem.CROSSBOW, ApplyToItem.SWORD));
    }
}
