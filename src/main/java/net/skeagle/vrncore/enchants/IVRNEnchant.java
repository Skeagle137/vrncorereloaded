package net.skeagle.vrncore.enchants;

import java.util.ArrayList;

public interface IVRNEnchant {

    String setDescription();

    int setRarityPoints();

    int setRarityFactor();

    ArrayList<ApplyToItem> setApplyToItems();
}