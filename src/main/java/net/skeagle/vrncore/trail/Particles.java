package net.skeagle.vrncore.trail;

import net.skeagle.vrncommands.misc.FormatUtils;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;

public enum Particles {
    FIREWORKS_SPARK(Particle.FIREWORKS_SPARK, Material.FIREWORK_ROCKET, ParticleProperties.DIRECTIONAL),
    WATER_SPLASH(Particle.WATER_SPLASH, Material.FISHING_ROD, ParticleProperties.DIRECTIONAL),
    VOID(Particle.TOWN_AURA, Material.BEDROCK, ParticleProperties.DIRECTIONAL),
    CRIT(Particle.CRIT, Material.IRON_SWORD, ParticleProperties.DIRECTIONAL),
    CRIT_MAGIC("Crit (Enchanted)", Particle.CRIT_MAGIC, Material.DIAMOND_SWORD, ParticleProperties.DIRECTIONAL),
    SMOKE(Particle.SMOKE_LARGE, Material.SMOKER, ParticleProperties.DIRECTIONAL),
    POTION("Potion Effect", Particle.SPELL_MOB, Material.POTION, ParticleProperties.COLOR),
    POTION_SPLASH("Splash Potion", Particle.SPELL_INSTANT, Material.SPLASH_POTION),
    WITCH("Witch Potion", Particle.SPELL_WITCH, Material.LINGERING_POTION),
    DRIP_WATER("Water Drip", Particle.DRIP_WATER, Material.WATER_BUCKET),
    DRIP_LAVA("Lava Drip", Particle.DRIP_LAVA, Material.LAVA_BUCKET),
    STORMY(Particle.VILLAGER_ANGRY, Material.FIRE_CHARGE),
    EMERALD(Particle.VILLAGER_HAPPY, Material.EMERALD, ParticleProperties.DIRECTIONAL),
    NOTE(Particle.NOTE, Material.NOTE_BLOCK, ParticleProperties.COLOR),
    ENCHANTMENT_GLYPH(Particle.ENCHANTMENT_TABLE, Material.ENCHANTING_TABLE, ParticleProperties.DIRECTIONAL),
    FLAME(Particle.FLAME, Material.CAMPFIRE, ParticleProperties.DIRECTIONAL),
    LAVA(Particle.LAVA, Material.MAGMA_BLOCK),
    REDSTONE("Redstone Dust", Particle.REDSTONE, Material.REDSTONE, ParticleProperties.COLOR),
    SNOW(Particle.SNOWBALL, Material.SNOWBALL),
    SLIME(Particle.SLIME, Material.SLIME_BALL),
    HEART(Particle.HEART, Material.POPPY),
    END_ROD(Particle.END_ROD, Material.END_ROD, ParticleProperties.DIRECTIONAL),
    SOUL(Particle.SOUL, Material.SOUL_SAND),
    SPORE_CRIMSON("Spore (Crimson)", Particle.CRIMSON_SPORE, Material.CRIMSON_HYPHAE, ParticleProperties.DIRECTIONAL),
    SPORE_WARPED("Spore (Warped)", Particle.WARPED_SPORE, Material.WARPED_HYPHAE, ParticleProperties.DIRECTIONAL),
    SOUL_FLAME(Particle.SOUL_FIRE_FLAME, Material.SOUL_CAMPFIRE, ParticleProperties.DIRECTIONAL),
    SPORE_BLOSSOM(Particle.SPORE_BLOSSOM_AIR, Material.FLOWERING_AZALEA, ParticleProperties.DIRECTIONAL),
    WAX_ON(Particle.WAX_ON, Material.WAXED_COPPER_BLOCK),
    WAX_OFF(Particle.WAX_OFF, Material.OXIDIZED_COPPER),
    ELECTRIC_SPARK(Particle.ELECTRIC_SPARK, Material.LIGHTNING_ROD, ParticleProperties.DIRECTIONAL),
    PORTAL(Particle.REVERSE_PORTAL, Material.PURPLE_WOOL, ParticleProperties.DIRECTIONAL),
    DAMAGE_INDICATOR(Particle.DAMAGE_INDICATOR, Material.STONE_AXE, ParticleProperties.DIRECTIONAL),
    FALLING_DUST(Particle.FALLING_DUST, Material.GUNPOWDER, ParticleProperties.DIRECTIONAL),
    TRANSITION_DUST(Particle.DUST_COLOR_TRANSITION, Material.MAGENTA_CONCRETE_POWDER, ParticleProperties.COLOR_TRANSITION);

    private final String name;
    private final Particle particle;
    private final Material mat;
    private ParticleProperties[] properties;

    Particles(final Particle particle, final Material mat) {
        this.name = FormatUtils.toTitleCase(this.name().replaceAll("_", " "));
        this.particle = particle;
        this.mat = mat;
    }

    Particles(final Particle particle, final Material mat, final ParticleProperties... properties) {
        this(particle, mat);
        this.properties = properties;
    }

    Particles(final String name, final Particle particle, final Material mat) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
    }

    Particles(final String name, final Particle particle, final Material mat, final ParticleProperties... properties) {
        this(name, particle, mat);
        this.properties = properties;
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

    public ParticleProperties[] getProperties() {
        return properties;
    }

    public static Particles getFromParticle(final Particle particle) {
        return Arrays.stream(values()).filter(t -> t.getParticle() == particle).findFirst().orElse(null);
    }

    public String getPermission(TrailType type) {
        return "vrn.trails." + toString().toLowerCase() + "." + type.name().toLowerCase();
    }

    public void run(Player player, Location loc, int amount, double speed, double off) {
        run(player, loc, amount, speed, off, off, off, new Particle.DustOptions(Color.RED, 1), TrailVisibility.ALL);
    }

    public void run(Player player, Location loc, int amount, double speed, double off, TrailVisibility visibility) {
        run(player, loc, amount, speed, off, off, off, new Particle.DustOptions(Color.RED, 1), visibility);
    }

    public void run(Player player, Location loc, int amount, double speed, double offX, double offY, double offZ, TrailVisibility visibility) {
        run(player, loc, amount, speed, offX, offY, offZ, new Particle.DustOptions(Color.RED, 1), visibility);
    }

    public void run(Player player, Location loc, int amount, double speed, double offX, double offY, double offZ, Particle.DustOptions options, TrailVisibility visibility) {
        boolean color = particle == Particle.REDSTONE || particle == Particle.DUST_COLOR_TRANSITION;
        if (visibility == TrailVisibility.CLIENT) {

            player.getWorld().spawnParticle(particle, loc, amount, offX, offY, offZ, speed, color ? options : null, true);
        }
        else {
            loc.getWorld().spawnParticle(particle, loc, amount, offX, offY, offZ, speed, color ? options : null, true);
        }
    }

    enum ParticleProperties {
        DIRECTIONAL,
        DATA,
        COLOR,
        COLOR_TRANSITION
    }
}