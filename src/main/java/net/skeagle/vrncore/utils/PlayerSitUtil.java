package net.skeagle.vrncore.utils;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public final class PlayerSitUtil {

    private final Player p;
    @Getter
    private static final HashMap<UUID, ArmorStand> sitmap = new HashMap<>();

    public static boolean containsStand(ArmorStand stand) {
        return sitmap.containsValue(stand);
    }

    public static PlayerSitUtil getPlayer(Player p) {
        return new PlayerSitUtil(p);
    }

    public PlayerSitUtil(Player p) {
        this.p = p;
    }

    public boolean isSitting() {
        return sitmap.containsKey(p.getUniqueId());
    }

    public void setSitting(final boolean b) {
        if (b && !isSitting()) {
            Location loc = p.getLocation();
            ArmorStand stand = loc.getWorld().spawn(loc.clone().subtract(0.0, 1.7, 0.0), ArmorStand.class);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setSilent(true);
            say(p, "You are now sitting.");
            stand.addPassenger(p);
            sitmap.put(p.getUniqueId(), stand);
        }
        else if (!b && isSitting()) {
            ArmorStand stand = sitmap.get(p.getUniqueId());
            say(p, "You are no longer sitting.");
            sitmap.remove(p.getUniqueId());
            p.eject();
            p.teleport(stand.getLocation().clone().add(0.0, 1.7, 0.0));
            stand.remove();
        }
    }
}
