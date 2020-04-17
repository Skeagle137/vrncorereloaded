package net.skeagle.vrncore.utils;

import org.mineacademy.fo.remain.CompMaterial;
import org.mineacademy.fo.remain.CompParticle;

public enum VRNParticle {
    FIREWORKS_SPARK("Spark", CompParticle.FIREWORKS_SPARK, CompMaterial.FIREWORK_ROCKET),
    WATER_SPLASH("Water Splash", CompParticle.WATER_SPLASH, CompMaterial.FISHING_ROD),
    VOID("Void", CompParticle.TOWN_AURA, CompMaterial.BEDROCK),
    CRIT("Crit", CompParticle.CRIT, CompMaterial.IRON_SWORD),
    CRIT_MAGIC("Crit (Enchanted)", CompParticle.CRIT_MAGIC, CompMaterial.DIAMOND_SWORD),
    SMOKE_LARGE("Smoke", CompParticle.SMOKE_LARGE, CompMaterial.CAMPFIRE),
    POTION("Potion Effect", CompParticle.SPELL, CompMaterial.POTION),
    POTION_SPLASH("Splash Potion", CompParticle.SPELL_INSTANT, CompMaterial.SPLASH_POTION),
    WITCH("Witch Potion", CompParticle.SPELL_WITCH, CompMaterial.LINGERING_POTION),
    DRIP_WATER("Water Drip", CompParticle.DRIP_WATER, CompMaterial.WATER_BUCKET),
    DRIP_LAVA("Lava Drip", CompParticle.DRIP_LAVA, CompMaterial.LAVA_BUCKET),
    STORMY("Stormy", CompParticle.VILLAGER_ANGRY, CompMaterial.FIRE_CHARGE),
    EMERALD("Emerald", CompParticle.VILLAGER_HAPPY, CompMaterial.EMERALD),
    NOTE("Note", CompParticle.NOTE, CompMaterial.NOTE_BLOCK),
    ENCHANT("Enchant Glyph", CompParticle.ENCHANTMENT_TABLE, CompMaterial.ENCHANTING_TABLE),
    FLAME("Flame", CompParticle.FLAME, CompMaterial.BLAZE_POWDER),
    LAVA("Lava", CompParticle.LAVA, CompMaterial.MAGMA_BLOCK),
    CLOUD("Cloud", CompParticle.CLOUD, CompMaterial.WHITE_WOOL),
    REDSTONE("Redstone Dust", CompParticle.REDSTONE, CompMaterial.REDSTONE),
    SNOWBALL("Snow", CompParticle.SNOWBALL, CompMaterial.SNOWBALL),
    SLIME("Slime", CompParticle.SLIME, CompMaterial.SLIME_BALL),
    HEART("Heart", CompParticle.HEART, CompMaterial.POPPY),
    END_ROD("End Rod Light", CompParticle.END_ROD, CompMaterial.END_ROD);

    private final String name;
    private final CompParticle particle;
    private final CompMaterial mat;

    VRNParticle(final String name, final CompParticle particle, final CompMaterial mat) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
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

    public static String getNameFromParticle(final CompParticle particle) {
        for (final VRNParticle vrn : VRNParticle.values()) {
            if (vrn.getParticle() == particle) {
                return vrn.name().toLowerCase();
            }
        }
        return null;
    }
}