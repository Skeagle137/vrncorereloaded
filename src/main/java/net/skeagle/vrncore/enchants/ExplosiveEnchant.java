package net.skeagle.vrncore.enchants;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.ProjectileHitEvent;

public class ExplosiveEnchant extends VRNenchant {

    @Getter
    private static final VRNenchant instance = new ExplosiveEnchant();

    private ExplosiveEnchant() {
        super("Explosive", 2, "Arrows explode when they hit an object or the ground.", 30, 10, ApplyToItem.BOW, ApplyToItem.CROSSBOW);
    }

    @Override
    protected void onHit(int level, LivingEntity shooter, int chanceFactor, ProjectileHitEvent e) {
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (level), false, false);
    }
}
