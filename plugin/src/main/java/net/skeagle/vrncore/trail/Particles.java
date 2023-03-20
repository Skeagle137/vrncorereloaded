package net.skeagle.vrncore.trail;

import net.skeagle.vrncommands.misc.FormatUtils;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.SuperVanishHook;
import net.skeagle.vrncore.playerdata.TrailData;
import org.bukkit.*;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;

public enum Particles {
    FIREWORKS_SPARK(Particle.FIREWORKS_SPARK, Material.FIREWORK_ROCKET, ParticleProperties.DIRECTIONAL),
    WATER_SPLASH(Particle.WATER_SPLASH, Material.FISHING_ROD),
    VOID(Particle.TOWN_AURA, Material.BEDROCK),
    CRIT(Particle.CRIT, Material.IRON_SWORD, 0.7),
    CRIT_MAGIC("Crit (Enchanted)", Particle.CRIT_MAGIC, Material.DIAMOND_SWORD, 0.7),
    SMOKE(Particle.SMOKE_LARGE, Material.SMOKER, ParticleProperties.DIRECTIONAL),
    POTION("Potion Effect", Particle.SPELL_MOB, Material.LINGERING_POTION, ParticleProperties.COLOR),
    WITCH("Witch Potion", Particle.SPELL_WITCH, Material.SPLASH_POTION),
    DRIP_WATER("Water Drip", Particle.DRIP_WATER, Material.WATER_BUCKET),
    DRIP_LAVA("Lava Drip", Particle.DRIP_LAVA, Material.LAVA_BUCKET),
    STORMY(Particle.VILLAGER_ANGRY, Material.FIRE_CHARGE),
    EMERALD(Particle.VILLAGER_HAPPY, Material.EMERALD),
    NOTE(Particle.NOTE, Material.NOTE_BLOCK, ParticleProperties.COLOR),
    ENCHANTMENT_GLYPH(Particle.ENCHANTMENT_TABLE, Material.ENCHANTING_TABLE, 1.2),
    FLAME(Particle.FLAME, Material.CAMPFIRE, ParticleProperties.DIRECTIONAL),
    LAVA(Particle.LAVA, Material.MAGMA_BLOCK),
    REDSTONE("Redstone Dust", Particle.REDSTONE, Material.REDSTONE, ParticleProperties.COLOR),
    SNOW(Particle.SNOWBALL, Material.SNOWBALL),
    SLIME(Particle.SLIME, Material.SLIME_BALL),
    HEART(Particle.HEART, Material.POPPY),
    END_ROD(Particle.END_ROD, Material.END_ROD, 0.12),
    SOUL(Particle.SOUL, Material.SOUL_SAND, 0.1),
    SPORE_CRIMSON("Spore (Crimson)", Particle.CRIMSON_SPORE, Material.CRIMSON_HYPHAE),
    SPORE_WARPED("Spore (Warped)", Particle.WARPED_SPORE, Material.WARPED_HYPHAE),
    SOUL_FLAME(Particle.SOUL_FIRE_FLAME, Material.SOUL_CAMPFIRE, ParticleProperties.DIRECTIONAL),
    SPORE_BLOSSOM(Particle.SPORE_BLOSSOM_AIR, Material.FLOWERING_AZALEA),
    WAX_ON(Particle.WAX_ON, Material.WAXED_COPPER_BLOCK, 12.0),
    WAX_OFF(Particle.WAX_OFF, Material.OXIDIZED_COPPER, 12.0),
    PORTAL(Particle.PORTAL, Material.OBSIDIAN, 1.0),
    REVERSE_PORTAL(Particle.REVERSE_PORTAL, Material.CRYING_OBSIDIAN, 0.1),
    DAMAGE_INDICATOR(Particle.DAMAGE_INDICATOR, Material.STONE_AXE, 0.45),
    TRANSITION_DUST(Particle.DUST_COLOR_TRANSITION, Material.MAGENTA_CONCRETE_POWDER, ParticleProperties.COLOR_TRANSITION),
    FALLING_NECTAR(Particle.FALLING_NECTAR, Material.BEEHIVE),
    FALLING_HONEY(Particle.FALLING_HONEY, Material.BEE_NEST),
    SMALL_FLAME(Particle.SMALL_FLAME, Material.CANDLE, 0.1);


    private final String name;
    private final Particle particle;
    private final Material mat;
    private double spreadSpeed;
    private ParticleProperties[] properties = {};

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

    Particles(final Particle particle, final Material mat, final double spreadSpeed) {
        this(particle, mat, ParticleProperties.DIRECTIONAL);
        this.spreadSpeed = spreadSpeed;
    }

    Particles(final String name, final Particle particle, final Material mat, final double spreadSpeed) {
        this(name, particle, mat, ParticleProperties.DIRECTIONAL);
        this.spreadSpeed = spreadSpeed;
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

    public double getSpreadSpeed() {
        return spreadSpeed;
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

    public void run(Player player, Location loc, TrailData data, int amount, double speed, double off) {
        run(player, loc, data, amount, speed, off, TrailVisibility.ALL);
    }

    public void run(Player player, Location loc, TrailData data, int amount, double speed, double off, TrailVisibility visibility) {
        run(player, loc, data, amount, speed, off, off, off, visibility);
    }

    public void run(Player player, Location loc, TrailData data, int amount, double speed, double offX, double offY, double offZ, TrailVisibility visibility) {
        Object particleOptions = null;
        if (this.getProperties().length != 0) {
            List<ParticleProperties> props = Arrays.asList(this.getProperties());
            if (props.contains(ParticleProperties.COLOR)) {
                switch (this) {
                    case REDSTONE -> particleOptions = new Particle.DustOptions(data.getColor(), (float) data.getSize());
                    case NOTE -> {
                        amount = 0;
                        speed = 1;
                        offX = data.getNote() / 24D;
                        offY = 0;
                        offZ = 0;
                    }
                    case POTION -> {
                        speed = 1;
                        offX = data.getColor().getRed() / 255D;
                        offY = data.getColor().getGreen() / 255D;
                        offZ = data.getColor().getBlue() / 255D;
                    }
                }
            } else if (props.contains(ParticleProperties.COLOR_TRANSITION)) {
                particleOptions = new Particle.DustTransition(data.getColor(), data.getFade(), (float) data.getSize());
            }
        }
        if (this == POTION) {
            for (int i = 0; i < amount; i++) {
                run(player, loc, 0, speed, offX, offY, offZ, particleOptions, visibility);
            }
            return;
        }
        run(player, loc, amount, speed, offX, offY, offZ, particleOptions, visibility);
    }

    private void run(Player player, Location location, int amount, double speed, double offX, double offY, double offZ, Object options, TrailVisibility visibility) {
        if (visibility == TrailVisibility.CLIENT) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (HookManager.isSuperVanishLoaded() && !SuperVanishHook.canSee(pl, player)) {
                    continue;
                }
                pl.spawnParticle(this.getParticle(), location, amount, offX, offY, offZ, speed, options);
            }
        }
        else {
            player.getWorld().spawnParticle(this.getParticle(), location, amount, offX, offY, offZ, speed, options, true);
        }

    }

    public enum ParticleProperties {
        DIRECTIONAL,
        COLOR,
        COLOR_TRANSITION
    }
}