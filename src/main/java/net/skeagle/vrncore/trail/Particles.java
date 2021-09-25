package net.skeagle.vrncore.trail;

import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Locale;

public enum Particles {
    FIREWORKS_SPARK("Spark", Particle.FIREWORKS_SPARK, Material.FIREWORK_ROCKET, true),
    WATER_SPLASH("Water Splash", Particle.WATER_SPLASH, Material.FISHING_ROD, true),
    VOID("Void", Particle.TOWN_AURA, Material.BEDROCK, true),
    CRIT("Crit", Particle.CRIT, Material.IRON_SWORD, true),
    CRIT_MAGIC("Crit (Enchanted)", Particle.CRIT_MAGIC, Material.DIAMOND_SWORD, true),
    SMOKE("Smoke", Particle.SMOKE_LARGE, Material.SMOKER, true),
    POTION("Potion Effect", Particle.SPELL_MOB, Material.POTION, false, false, true),
    POTION_SPLASH("Splash Potion", Particle.SPELL_INSTANT, Material.SPLASH_POTION),
    WITCH("Witch Potion", Particle.SPELL_WITCH, Material.LINGERING_POTION),
    DRIP_WATER("Water Drip", Particle.DRIP_WATER, Material.WATER_BUCKET),
    DRIP_LAVA("Lava Drip", Particle.DRIP_LAVA, Material.LAVA_BUCKET),
    STORMY("Stormy", Particle.VILLAGER_ANGRY, Material.FIRE_CHARGE),
    EMERALD("Emerald", Particle.VILLAGER_HAPPY, Material.EMERALD, true),
    NOTE("Note", Particle.NOTE, Material.NOTE_BLOCK, true),
    ENCHANT("Enchant Glyph", Particle.ENCHANTMENT_TABLE, Material.ENCHANTING_TABLE, true),
    FLAME("Flame", Particle.FLAME, Material.CAMPFIRE, true),
    LAVA("Lava", Particle.LAVA, Material.MAGMA_BLOCK),
    REDSTONE("Redstone Dust", Particle.REDSTONE, Material.REDSTONE, false, false, true),
    SNOWBALL("Snow", Particle.SNOWBALL, Material.SNOWBALL),
    SLIME("Slime", Particle.SLIME, Material.SLIME_BALL),
    HEART("Heart", Particle.HEART, Material.POPPY),
    END_ROD("End Rod Light", Particle.END_ROD, Material.END_ROD, true),
    SOUL("Soul", Particle.SOUL, Material.SOUL_SAND),
    SPORE_CRIMSON("Spore (Crimson)", Particle.CRIMSON_SPORE, Material.CRIMSON_HYPHAE, true),
    SPORE_WARPED("Spore (Warped)", Particle.WARPED_SPORE, Material.WARPED_HYPHAE, true),
    SOUL_FLAME("Soul Flame", Particle.SOUL_FIRE_FLAME, Material.SOUL_CAMPFIRE, true),
    SPORE_BLOSSOM("Spore Blossom", Particle.SPORE_BLOSSOM_AIR, Material.FLOWERING_AZALEA, true),
    WAX_ON("Wax On", Particle.WAX_ON, Material.WAXED_COPPER_BLOCK),
    WAX_OFF("Wax Off", Particle.WAX_OFF, Material.OXIDIZED_COPPER),
    ELECTRIC("Electric Spark", Particle.ELECTRIC_SPARK, Material.LIGHTNING_ROD, true),
    PORTAL("Portal", Particle.REVERSE_PORTAL, Material.PURPLE_WOOL, true),
    DAMAGE_INDICATOR("Damage Indicator", Particle.DAMAGE_INDICATOR, Material.STONE_AXE, true),
    FALLING_DUST("Falling Dust", Particle.FALLING_DUST, Material.GUNPOWDER, true),
    TRANSITION_DUST("Transition Dust", Particle.DUST_COLOR_TRANSITION, Material.MAGENTA_CONCRETE_POWDER, false, false, false, true);

    private final String name;
    private final Particle particle;
    private final Material mat;
    private final boolean directional;
    private final boolean data;
    private final boolean colorable;
    private final boolean colorTransition;

    Particles(final String name, final Particle particle, final Material mat) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
        this.directional = false;
        this.data = false;
        this.colorable = false;
        this.colorTransition = false;
    }

    Particles(final String name, final Particle particle, final Material mat, final boolean directional) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
        this.directional = directional;
        this.data = false;
        this.colorable = false;
        this.colorTransition = false;
    }

    Particles(final String name, final Particle particle, final Material mat, final boolean directional, final boolean data, final boolean colorable) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
        this.directional = directional;
        this.data = data;
        this.colorable = colorable;
        this.colorTransition = false;
    }

    Particles(final String name, final Particle particle, final Material mat, final boolean directional, final boolean data, final boolean colorable, final boolean colorTransition) {
        this.name = name;
        this.particle = particle;
        this.mat = mat;
        this.directional = directional;
        this.data = data;
        this.colorable = colorable;
        this.colorTransition = colorTransition;
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

    public boolean isDirectional() {
        return directional;
    }

    public boolean hasData() {
        return data;
    }

    public boolean isColorable() {
        return colorable;
    }

    public boolean hasColorTransition() {
        return colorTransition;
    }

    public static Particles getFromParticle(final Particle particle) {
        return Arrays.stream(values()).filter(t -> t.getParticle() == particle).findFirst().orElse(null);
    }

    public String getPermission(TrailType type) {
        return "vrn." + type.name + "trails." + toString().toLowerCase(Locale.ROOT);
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
}