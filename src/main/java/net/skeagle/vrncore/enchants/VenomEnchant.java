package net.skeagle.vrncore.enchants;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;

public class VenomEnchant extends VRNenchant {

    @Getter
    private static final VRNenchant instance = new VenomEnchant();

    private VenomEnchant() {
        super("Venom", 5, "Inflicts the target with poison. The greater the enchant's power, the longer the effect lasts.",
                15, 5, ApplyToItem.BOW, ApplyToItem.SWORD);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, int chanceFactor, EntityDamageByEntityEvent e) {

    }

    @Override
    protected void onHit(int level, LivingEntity shooter, int chanceFactor, ProjectileHitEvent e) {

    }
}
