package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.GenericEvent;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.model.SimpleEnchantment;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;

import java.util.ArrayList;
import java.util.Collections;

import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public class ExecuteEnchant extends SimpleEnchantment implements IVRNEnchant {

    @Getter
    private static final Enchantment instance = new ExecuteEnchant();

    private ExecuteEnchant() {
        super("Execute", 5);
    }

    @Override
    protected void onDamage(final int level, final LivingEntity damager, final EntityDamageByEntityEvent e) {
        if (!(e instanceof LivingEntity)) return;
        final GenericEvent ge = new GenericEvent();
        if (ge.calcChance(8, level)) {
            if (((LivingEntity) e.getEntity()).getHealth() < 1 + level) {
                ((LivingEntity) e.getEntity()).setHealth(0);
                CompParticle.FLAME.spawn(e.getEntity().getLocation().add(0, 1, 0));
                CompSound.WITHER_HURT.play(e.getEntity().getLocation(), 1, 0.8F);
                if (e.getEntity() instanceof Player) {
                    sayNoPrefix(e.getEntity(), "&c&lEXECUTED &7by " + damager.getName() + "'s insta-kill below &c" + (1 + level) + "❤");
                }
            }
        }
    }

    @Override
    public String setDescription() {
        return "Chance to immediately kill the target if it has a specific amount of health left.";
    }

    @Override
    public int setRarityPoints() {
        return 250;
    }

    @Override
    public int setRarityFactor() {
        return 20;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.SWORD));
    }
}
