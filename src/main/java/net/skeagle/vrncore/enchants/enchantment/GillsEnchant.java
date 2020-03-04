package net.skeagle.vrncore.enchants.enchantment;

import lombok.Getter;
import net.skeagle.vrncore.enchants.ApplyToItem;
import net.skeagle.vrncore.enchants.IVRNEnchant;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.model.SimpleEnchantment;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Map;

import static net.skeagle.vrncore.utils.VRNUtil.sayActionBar;

public class GillsEnchant extends SimpleEnchantment implements IVRNEnchant, Listener {
    @Getter
    private static final Enchantment instance = new GillsEnchant();

    private GillsEnchant() {
        super("Gills", 2);
    }

    private final ArrayList<Player> its_just_a_one_time_thing = new ArrayList<>();

    @EventHandler
    public void onDamage(final EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            final Player p = (Player) e.getEntity();
            final ItemStack item = p.getEquipment().getBoots();
            final Map enchants = SimpleEnchantment.findEnchantments(item);
            if (item != null && enchants.containsKey(this)) {
                if (e.getCause() == EntityDamageEvent.DamageCause.DROWNING) {
                    if (p.getRemainingAir() == 0) {
                        if (!its_just_a_one_time_thing.contains(p)) {
                            e.setCancelled(true);
                            p.setRemainingAir(p.getMaximumAir());
                            its_just_a_one_time_thing.add(p);
                            Common.runLater(20 * (60 / (int) enchants.get(this)), () -> {
                                its_just_a_one_time_thing.remove(p);
                                sayActionBar(p, "&aGills Replenished!");
                            });
                        }
                    }
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
        return 50;
    }

    @Override
    public ArrayList<ApplyToItem> setApplyToItems() {
        return new ArrayList<>(Collections.singletonList(ApplyToItem.BOOTS));
    }
}
