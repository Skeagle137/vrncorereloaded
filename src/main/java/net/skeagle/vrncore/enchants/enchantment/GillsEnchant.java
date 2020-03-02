package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Collections;

public class GillsEnchant extends SimpleEnchantment implements IVRNEnchant, Listener {
    @Getter
    private static final Enchantment instance = new GillsEnchant();

    private GillsEnchant() {
        super("Gills", 1);
    }

    @EventHandler
    public void onInventory(final EntityDamageEvent e) {
        if (!(e instanceof LivingEntity)) return;
        if (e instanceof Player) {
            if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                final Player p = (Player) e.getEntity();
                final ItemStack item = p.getEquipment().getBoots();

                if (item != null && item.containsEnchantment(this)) {
                    e.setCancelled(true);
                    p.setRemainingAir(p.getMaximumAir());
                }
            }
        }
    }

    @Override
    public String setDescription() {
        return "Refills your air bar if you run out of air underwater. Only applies once until you come up for air.";
    }

    @Override
    public int setRarityPoints() {
        return 300;
    }

    @Override
    public int setRarityFactor() {
        return 0;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.BOOTS));
    }
}
