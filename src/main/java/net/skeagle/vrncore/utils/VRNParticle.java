package net.skeagle.vrncore.utils;

import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompParticle;

public enum VRNParticle {
    EXPLOSION_LARGE("Explosion", CompParticle.EXPLOSION_LARGE, CompMaterial.TNT, false),
    EXPLOSION_HUGE("Big Explosion", CompParticle.EXPLOSION_HUGE, CompMaterial.TNT, false),
    FIREWORKS_SPARK("Spark", CompParticle.FIREWORKS_SPARK, CompMaterial.FIREWORK_ROCKET, true),
    WATER_SPLASH("Water Splash", CompParticle.WATER_SPLASH, CompMaterial.WATER_BUCKET, true),
    WATER_WAKE("Fishing", CompParticle.WATER_WAKE, CompMaterial.FISHING_ROD, false),
    VOID("Void", CompParticle.TOWN_AURA, CompMaterial.BEDROCK, true),
    CRIT("Crit", CompParticle.CRIT, CompMaterial.IRON_SWORD, true),
    CRIT_MAGIC("Crit (Enchanted)", CompParticle.CRIT_MAGIC, CompMaterial.DIAMOND_SWORD, true),
    SMOKE_LARGE("Smoke", CompParticle.SMOKE_LARGE, CompMaterial.CAMPFIRE, true),
    POTION("Potion Effect", CompParticle.SPELL, CompMaterial.POTION, true),
    POTION_SPLASH("Splash Potion", CompParticle.SPELL_INSTANT, CompMaterial.SPLASH_POTION, true),
    WITCH("Witch Potion", CompParticle.SPELL_WITCH, CompMaterial.LINGERING_POTION, true),
    DRIP_WATER("Water Drip", CompParticle.DRIP_WATER, CompMaterial.WATER_BUCKET, true),
    DRIP_LAVA("Lava Drip", CompParticle.DRIP_LAVA, CompMaterial.LAVA_BUCKET, true),
    STORMY("Stormy", CompParticle.VILLAGER_ANGRY, CompMaterial.FIRE_CHARGE, true),
    EMERALD("Emerald", CompParticle.VILLAGER_HAPPY, CompMaterial.EMERALD, true),
    NOTE("Note", CompParticle.NOTE, CompMaterial.NOTE_BLOCK, true),
    PORTAL("Portal", CompParticle.PORTAL, CompMaterial.OBSIDIAN, true),
    ENCHANT("Enchant Glyph", CompParticle.ENCHANTMENT_TABLE, CompMaterial.ENCHANTING_TABLE, true),
    FLAME("Flame", CompParticle.FLAME, CompMaterial.BLAZE_POWDER, true),
    LAVA("Lava", CompParticle.LAVA, CompMaterial.MAGMA_BLOCK, true),
    FOOTSTEP("Footstep", CompParticle.FOOTSTEP, CompMaterial.LEATHER_BOOTS, false),
    CLOUD("Cloud", CompParticle.CLOUD, CompMaterial.WHITE_WOOL, true),
    REDSTONE("Redstone Dust", CompParticle.REDSTONE, CompMaterial.REDSTONE, true),
    SNOWBALL("Snow", CompParticle.SNOWBALL, CompMaterial.SNOWBALL, true),
    SLIME("Slime", CompParticle.SLIME, CompMaterial.SLIME_BALL, true),
    HEART("Heart", CompParticle.HEART, CompMaterial.POPPY, true),
    BARRIER("Barrier", CompParticle.BARRIER, CompMaterial.BARRIER, false),
    ITEM_CRACK("Item Crack", CompParticle.ITEM_CRACK, CompMaterial.STONE, false),
    BLOCK_CRACK("Block Crack", CompParticle.BLOCK_CRACK, CompMaterial.STONE, false),
    BLOCK_DUST("Block Dust", CompParticle.BLOCK_DUST, CompMaterial.STONE, false),
    DRAGON_BREATH("Dragon Breath", CompParticle.DRAGON_BREATH, CompMaterial.DRAGON_EGG, false),
    END_ROD("End Rod Light", CompParticle.END_ROD, CompMaterial.END_ROD, true),
    DAMAGE_INDICATOR("Damage Indicator", CompParticle.DAMAGE_INDICATOR, CompMaterial.DIAMOND_AXE, true),
    SWEEP_ATTACK("Sweep Attack", CompParticle.SWEEP_ATTACK, CompMaterial.GOLDEN_SWORD, false),
    FALLING_DUST("Falling Dust", CompParticle.FALLING_DUST, CompMaterial.SAND, false);

    private String name;
    private CompParticle particle;
    private CompMaterial mat;
    private boolean usable;

    VRNParticle(String name, CompParticle particle, CompMaterial mat, boolean usable) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
        this.usable = usable;
    }

    public String getParticleName() {
        return name;
    }
    public CompParticle getParticle() {
        return particle;
    }
    public CompMaterial getMaterial() {
        return mat;
    }
    public boolean isUsable() {
        return usable;
    }
}