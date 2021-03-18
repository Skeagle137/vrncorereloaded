package net.skeagle.vrncore.trail;

import org.bukkit.Material;
import org.bukkit.Particle;

public enum VRNParticle {
    FIREWORKS_SPARK("Spark", Particle.FIREWORKS_SPARK, Material.FIREWORK_ROCKET),
    WATER_SPLASH("Water Splash", Particle.WATER_SPLASH, Material.FISHING_ROD),
    VOID("Void", Particle.TOWN_AURA, Material.BEDROCK),
    CRIT("Crit", Particle.CRIT, Material.IRON_SWORD),
    CRIT_MAGIC("Crit (Enchanted)", Particle.CRIT_MAGIC, Material.DIAMOND_SWORD),
    SMOKE("Smoke", Particle.SMOKE_LARGE, Material.SMOKER),
    POTION("Potion Effect", Particle.SPELL, Material.POTION),
    POTION_SPLASH("Splash Potion", Particle.SPELL_INSTANT, Material.SPLASH_POTION),
    WITCH("Witch Potion", Particle.SPELL_WITCH, Material.LINGERING_POTION),
    DRIP_WATER("Water Drip", Particle.DRIP_WATER, Material.WATER_BUCKET),
    DRIP_LAVA("Lava Drip", Particle.DRIP_LAVA, Material.LAVA_BUCKET),
    STORMY("Stormy", Particle.VILLAGER_ANGRY, Material.FIRE_CHARGE),
    EMERALD("Emerald", Particle.VILLAGER_HAPPY, Material.EMERALD),
    NOTE("Note", Particle.NOTE, Material.NOTE_BLOCK),
    ENCHANT("Enchant Glyph", Particle.ENCHANTMENT_TABLE, Material.ENCHANTING_TABLE),
    FLAME("Flame", Particle.FLAME, Material.CAMPFIRE),
    LAVA("Lava", Particle.LAVA, Material.MAGMA_BLOCK),
    REDSTONE("Redstone Dust", Particle.REDSTONE, Material.REDSTONE),
    SNOWBALL("Snow", Particle.SNOWBALL, Material.SNOWBALL),
    SLIME("Slime", Particle.SLIME, Material.SLIME_BALL),
    HEART("Heart", Particle.HEART, Material.POPPY),
    END_ROD("End Rod Light", Particle.END_ROD, Material.END_ROD),
    SOUL("Soul", Particle.SOUL, Material.SOUL_SAND),
    SPORE_CRIMSON("Spore (Crimson)", Particle.CRIMSON_SPORE, Material.CRIMSON_HYPHAE),
    SPORE_WARPED("Spore (Warped)", Particle.WARPED_SPORE, Material.WARPED_HYPHAE),
    SOUL_FLAME("Soul Flame", Particle.SOUL_FIRE_FLAME, Material.SOUL_CAMPFIRE);


    private final String name;
    private final Particle particle;
    private final Material mat;

    VRNParticle(final String name, final Particle particle, final Material mat) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
    }

    public String getParticleName() {
        return name;
    }
    public Particle getParticle() {
        return particle;
    }
    public Material getMaterial() {
        return mat;
    }

    public static String getNameFromParticle(final Particle particle) {
        for (final VRNParticle vrn : VRNParticle.values())
            if (vrn.getParticle() == particle)
                return vrn.name().toLowerCase();
        return null;
    }
}