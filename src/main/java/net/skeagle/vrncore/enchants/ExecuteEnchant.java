package net.skeagle.vrncore.enchants;

import lombok.Getter;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.mineacademy.fo.remain.CompParticle;
import org.mineacademy.fo.remain.CompSound;

import java.util.Random;

import static org.mineacademy.fo.Common.tellNoPrefix;

public class ExecuteEnchant extends VRNenchant {

    @Getter
    private static final VRNenchant instance = new ExecuteEnchant();

    private ExecuteEnchant() {
        super("Execute", 5, "Chance to immediately kill the target if it has a specific amount of health left.", 160, 20, ApplyToItem.SWORD);
    }

    @Override
    protected void onDamage(int level, LivingEntity damager, int chanceFactor, EntityDamageByEntityEvent e) {
        if (!(e.getEntity() instanceof LivingEntity))
            return;

        chanceFactor = 8 * level;
        Random random = new Random();
        if (random.nextInt(101) <= chanceFactor) {
            if (((LivingEntity) e.getEntity()).getHealth() < 1 + level) {
                e.setDamage(200);
                CompParticle.CRIT.spawn(e.getEntity().getLocation());
                if (e.getEntity() instanceof Player) {
                    CompSound.WITHER_HURT.play(e.getEntity().getLocation(), 20, 0.5F);
                    tellNoPrefix(e.getEntity(), "&c&lEXECUTED &7by " + damager.getName() + "'s insta-kill below &c" + (1 + level) + "❤");
                }
            }
        }
    }
}
