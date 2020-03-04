package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.GenericEvent;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Arrays;

public class VenomEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new VenomEnchant();

    private VenomEnchant() {
        super("Venom", 5);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        final GenericEvent ge = new GenericEvent();
        if (ge.calcChance(15)) {
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.POISON, level, 1, false, false, true));
        }
    }

    @Override
    protected void onHit(final int level, final LivingEntity shooter, final ProjectileHitEvent e) {
        e.getEntity().getWorld().createExplosion(e.getEntity().getLocation(), (float) (level), false, false);
    }

    @Override
    public String setDescription() {
        return "15% chance to inflict the target with poison. The greater the enchant's power, the longer the effect lasts.";
    }

    @Override
    public int setRarityPoints() {
        return 70;
    }

    @Override
    public int setRarityFactor() {
        return 5;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Arrays.asList(ApplyToItem.BOW, ApplyToItem.CROSSBOW, ApplyToItem.SWORD));
    }
}
