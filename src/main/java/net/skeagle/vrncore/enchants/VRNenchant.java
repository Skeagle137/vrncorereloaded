package net.skeagle.vrncore.enchants;

import lombok.Getter;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.CompMaterial;

public class VRNenchant extends SimpleEnchantment {
    private int chance;
    private String description;
    private int rarityPoints;
    private int rarityFactor;
    private ApplyToItem[] canApplyToItems;

    @Getter
    private static final Enchantment instance = new VRNenchant();

    VRNenchant(final String name, final int maxLevel, String description, int rarityPoints, int rarityFactor, ApplyToItem... canApplyToItems) {
        super(name, maxLevel);
        this.description = description;
        this.rarityPoints = rarityPoints;
        this.rarityFactor = rarityFactor;
        this.canApplyToItems = canApplyToItems;
    }

    private VRNenchant() {
        super("plcehlder", 0);
    }

    protected enum ApplyToItem {
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
        ALL_ARMOR(HELMET, CHESTPLATE, LEGGINGS, BOOTS),
        ALL_TOOLS(PICKAXE, AXE, SHOVEL, HOE, SHEARS),
        ALL_PVP(SWORD, BOW, ROD, CROSSBOW),
        ANY(CompMaterial.values());

        private CompMaterial[] appliesTo;

        ApplyToItem(CompMaterial... appliesTo) {
            this.appliesTo = appliesTo;
        }

        ApplyToItem(ApplyToItem helmet, ApplyToItem chestplate, ApplyToItem leggings, ApplyToItem boots) {
        }

        ApplyToItem(ApplyToItem pickaxe, ApplyToItem axe, ApplyToItem shovel, ApplyToItem hoe, ApplyToItem shears) {
        }

        public CompMaterial[] getAppliesTo() {
            return appliesTo;
        }
    }

    protected enum Rarity {
        COMMON("&7Common (1✫)", 0),
        UNCOMMON("&aUncommon (2✫)", 50),
        RARE("&9Rare (3✫)", 100),
        EPIC("&d&oEpic &r&d(4✫)", 200),
        LEGENDARY("&6&lLegendary &r&6(5✫)", 400),
        MYTHICAL("&5&lMythical &r&5(6✫)", 800),
        ANCIENT("&4&l&k:&r&c&lAncient&r&4&l&k: &r&c(7✫)", 1600);

        private String rarity;
        private int pointsRequired;

        Rarity(String rarity, int pointsRequired) {
            this.rarity = rarity;
            this.pointsRequired = pointsRequired;
        }

        public String getRarity() {
            return rarity;
        }

        public int getPointsRequired() {
            return pointsRequired;
        }

        public String getRarityFromPoints(Rarity rarity) {
            for (Rarity r : Rarity.values()) {
                if (r.getPointsRequired() > rarity.getPointsRequired()) return rarity.getRarity();
            }
            return ANCIENT.getRarity();
        }
    }

    protected void onDamage(int level, LivingEntity damager, int chanceFactor, EntityDamageByEntityEvent e) {

    }

    protected void onInteract(int level, int chanceFactor, PlayerInteractEvent e) {

    }

    protected void onBreakBlock(int level, int chanceFactor, BlockBreakEvent e) {

    }

    protected void onShoot(int level, LivingEntity shooter, int chanceFactor, ProjectileLaunchEvent e) {

    }

    protected void onHit(int level, LivingEntity shooter, int chanceFactor, ProjectileHitEvent e) {
    }

    @Override
    protected final void onDamage(int level, LivingEntity damager, EntityDamageByEntityEvent event) {
        onDamage(level, damager, chance, event);
    }

    @Override
    protected final void onInteract(int level, PlayerInteractEvent event) {
        onInteract(level, chance, event);
    }

    @Override
    protected final void onBreakBlock(int level, BlockBreakEvent event) {
        onBreakBlock(level, chance, event);
    }

    @Override
    protected final void onShoot(int level, LivingEntity shooter, ProjectileLaunchEvent event) {
        onShoot(level, shooter, chance, event);
    }

    @Override
    protected final void onHit(int level, LivingEntity shooter, ProjectileHitEvent event) {
        onHit(level, shooter, chance, event);
    }


}
