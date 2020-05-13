package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Arrays;

public class ExplosiveEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new ExplosiveEnchant();

    private ExplosiveEnchant() {
        super("Explosive", 1);
    }

    @Override
    protected void onHit(final int level, final LivingEntity shooter, final ProjectileHitEvent e) {
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), 1, false, false);
    }

    @Override
    public String setDescription() {
        return "Arrows explode when they hit an object or the ground.";
    }

    @Override
    public int setRarityPoints() {
        return 70;
    }

    @Override
    public int setRarityFactor() {
        return 130;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Arrays.asList(ApplyToItem.BOW, ApplyToItem.CROSSBOW));
    }
}
