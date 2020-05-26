package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.GenericEvent;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.Remain;

import java.util.ArrayList;
import java.util.Collections;

public class StunEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new StunEnchant();

    private StunEnchant() {
        super("Stun", 3);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity)) return;

        final GenericEvent ge = new GenericEvent();
        if (ge.calcChance(35 + (level * 5))) {
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW, (level > 2 ? 3 : 2) * 20, 10, false, true, true));
            ((LivingEntity) e.getEntity()).addPotionEffect(new PotionEffect(PotionEffectType.SLOW_DIGGING, (level > 2 ? 3 : 2) * 20, 10, false, true, true));
            if (e.getEntity() instanceof Player) {
                Remain.sendTitle((Player) e.getEntity(), 0, (level > 2 ? 4 : 3) * 20, 1,
                        "&c&lStunned!", "&cCannot move from " + e.getDamager().getName() + "\'s stun!");
            }
        }

    }

    @Override
    public String setDescription() {
        return "40% chance to stun the target. Higher levels have a higher chance of stunning.";
    }

    @Override
    public int setRarityPoints() {
        return 150;
    }

    @Override
    public int setRarityFactor() {
        return 50;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.SWORD));
    }
}
