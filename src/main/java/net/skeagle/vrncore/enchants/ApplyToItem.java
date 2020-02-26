package net.skeagle.vrncore.enchants;

import org.mineacademy.fo.remain.CompMaterial;

public enum ApplyToItem {
    HELMET(CompMaterial.DIAMOND_HELMET, CompMaterial.GOLDEN_HELMET, CompMaterial.IRON_HELMET, CompMaterial.CHAINMAIL_HELMET, CompMaterial.LEATHER_HELMET),
    CHESTPLATE(CompMaterial.DIAMOND_CHESTPLATE, CompMaterial.GOLDEN_CHESTPLATE, CompMaterial.IRON_CHESTPLATE, CompMaterial.CHAINMAIL_CHESTPLATE, CompMaterial.LEATHER_CHESTPLATE),
    LEGGINGS(CompMaterial.DIAMOND_LEGGINGS, CompMaterial.GOLDEN_LEGGINGS, CompMaterial.IRON_LEGGINGS, CompMaterial.CHAINMAIL_LEGGINGS, CompMaterial.LEATHER_LEGGINGS),
    BOOTS(CompMaterial.DIAMOND_BOOTS, CompMaterial.GOLDEN_BOOTS, CompMaterial.IRON_BOOTS, CompMaterial.CHAINMAIL_BOOTS, CompMaterial.LEATHER_BOOTS),
    SWORD(CompMaterial.DIAMOND_SWORD, CompMaterial.GOLDEN_SWORD, CompMaterial.IRON_SWORD, CompMaterial.STONE_SWORD, CompMaterial.WOODEN_SWORD),
    PICKAXE(CompMaterial.DIAMOND_PICKAXE, CompMaterial.GOLDEN_PICKAXE, CompMaterial.IRON_PICKAXE, CompMaterial.STONE_PICKAXE, CompMaterial.WOODEN_PICKAXE),
    AXE(CompMaterial.DIAMOND_AXE, CompMaterial.GOLDEN_AXE, CompMaterial.IRON_AXE, CompMaterial.STONE_AXE, CompMaterial.WOODEN_AXE),
    SHOVEL(CompMaterial.DIAMOND_SHOVEL, CompMaterial.GOLDEN_SHOVEL, CompMaterial.IRON_SHOVEL, CompMaterial.STONE_SHOVEL, CompMaterial.WOODEN_SHOVEL),
    HOE(CompMaterial.DIAMOND_HOE, CompMaterial.GOLDEN_HOE, CompMaterial.IRON_HOE, CompMaterial.STONE_HOE, CompMaterial.WOODEN_HOE),
    BOW(CompMaterial.BOW),
    SHEARS(CompMaterial.SHEARS),
    ROD(CompMaterial.FISHING_ROD),
    ELYTRA(CompMaterial.ELYTRA),
    CROSSBOW(CompMaterial.CROSSBOW),
    ANY(CompMaterial.values());

    private CompMaterial[] appliesTo;

    ApplyToItem(CompMaterial... appliesTo) {
        this.appliesTo = appliesTo;
    }

    public CompMaterial[] getAppliesTo() {
        return appliesTo;
    }
}