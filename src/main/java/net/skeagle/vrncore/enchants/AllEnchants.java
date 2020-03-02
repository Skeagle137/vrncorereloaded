package net.skeagle.vrncore.enchants;

import net.skeagle.vrncore.enchants.enchantment.ExecuteEnchant;
import net.skeagle.vrncore.enchants.enchantment.ExplosiveEnchant;
import net.skeagle.vrncore.enchants.enchantment.GillsEnchant;
import net.skeagle.vrncore.enchants.enchantment.VenomEnchant;
import org.bukkit.enchantments.Enchantment;

public enum AllEnchants {
    PROTECTION_ENVIRONMENTAL("protection", Enchantment.PROTECTION_ENVIRONMENTAL, false),
    PROTECTION_FIRE("fire_protection", Enchantment.PROTECTION_FIRE, false),
    PROTECTION_FALL("feather_falling", Enchantment.PROTECTION_FALL, false),
    PROTECTION_EXPLOSIONS("blast_protection", Enchantment.PROTECTION_EXPLOSIONS, false),
    PROTECTION_PROJECTILE("projectile_protection", Enchantment.PROTECTION_PROJECTILE, false),
    OXYGEN("respiration", Enchantment.OXYGEN, false),
    WATER_WORKER("aqua_affinity", Enchantment.WATER_WORKER, false),
    THORNS("thorns", Enchantment.THORNS, false),
    DEPTH_STRIDER("depth_strider", Enchantment.DEPTH_STRIDER, false),
    FROST_WALKER("frost_walker", Enchantment.FROST_WALKER, false),
    DAMAGE_ALL("sharpness", Enchantment.DAMAGE_ALL, false),
    DAMAGE_UNDEAD("smite", Enchantment.DAMAGE_UNDEAD, false),
    DAMAGE_ARTHROPODS("bane_of_arthropods", Enchantment.DAMAGE_ARTHROPODS, false),
    KNOCKBACK("knockback", Enchantment.KNOCKBACK, false),
    FIRE_ASPECT("fire_aspect", Enchantment.FIRE_ASPECT, false),
    LOOT_BONUS_MOBS("looting", Enchantment.LOOT_BONUS_MOBS, false),
    SWEEPING_EDGE("sweeping", Enchantment.SWEEPING_EDGE, false),
    DIG_SPEED("efficiency", Enchantment.DIG_SPEED, false),
    SILK_TOUCH("silk_touch", Enchantment.SILK_TOUCH, false),
    DURABILITY("unbreaking", Enchantment.DURABILITY, false),
    LOOT_BONUS_BLOCKS("fortune", Enchantment.LOOT_BONUS_BLOCKS, false),
    ARROW_DAMAGE("power", Enchantment.ARROW_DAMAGE, false),
    ARROW_KNOCKBACK("punch", Enchantment.ARROW_KNOCKBACK, false),
    ARROW_FIRE("flame", Enchantment.ARROW_FIRE, false),
    ARROW_INFINITE("infinity", Enchantment.ARROW_INFINITE, false),
    LUCK("luck_of_the_sea", Enchantment.LUCK, false),
    LURE("lure", Enchantment.LURE, false),
    LOYALTY("loyalty", Enchantment.LOYALTY, false),
    IMPALING("impaling", Enchantment.IMPALING, false),
    RIPTIDE("riptide", Enchantment.RIPTIDE, false),
    CHANNELING("channeling", Enchantment.CHANNELING, false),
    MULTISHOT("multishot", Enchantment.MULTISHOT, false),
    QUICK_CHARGE("quick_charge", Enchantment.QUICK_CHARGE, false),
    PIERCING("piercing", Enchantment.PIERCING, false),
    MENDING("mending", Enchantment.MENDING, false),
    VANISHING_CURSE("vanishing_curse", Enchantment.VANISHING_CURSE, false),
    BINDING_CURSE("binding_curse", Enchantment.BINDING_CURSE, false),
    EXECUTE("execute", ExecuteEnchant.getInstance(), true),
    EXPLOSIVE("explosive", ExplosiveEnchant.getInstance(), true),
    VENOM("venom", VenomEnchant.getInstance(), true),
    GILLS("gills", GillsEnchant.getInstance(), true),
    ;

    private final String name;
    private final Enchantment enchant;
    private final boolean custom;

    AllEnchants(final String name, final Enchantment enchant, final boolean custom) {
        this.name = name;
        this.enchant = enchant;
        this.custom = custom;
    }

    public String getName() {
        return name;
    }

    public Enchantment getEnchant() {
        return enchant;
    }

    public boolean isCustom() {
        return custom;
    }

}