package net.skeagle.vrncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.UUID;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public final class PlayerSitUtil {

    private final Player p;

    private static final HashMap<UUID, UUID> sitMap = new HashMap<>();

    public static boolean containsStand(final ArmorStand stand) {
        return sitMap.containsValue(stand.getUniqueId());
    }

    public static PlayerSitUtil getPlayer(final Player p) {
        return new PlayerSitUtil(p);
    }

    public static HashMap<UUID, UUID> getSitMap() {
        return sitMap;
    }

    public PlayerSitUtil(final Player p) {
        this.p = p;
    }

    public boolean isSitting() {
        return sitMap.containsKey(p.getUniqueId());
    }

    public void setSitting(final boolean b) {
        if (b && !isSitting()) {
            final Location loc = p.getLocation();
            final ArmorStand stand = loc.getWorld().spawn(loc.clone().subtract(0.0, 1.7, 0.0), ArmorStand.class);
            stand.setGravity(false);
            stand.setVisible(false);
            stand.setSilent(true);
            say(p, "You are now sitting.");
            stand.addPassenger(p);
            sitMap.put(p.getUniqueId(), stand.getUniqueId());
        }
        else if (!b && isSitting()) {
            final ArmorStand stand = (ArmorStand) Bukkit.getEntity(sitMap.get(p.getUniqueId()));
            say(p, "You are no longer sitting.");
            sitMap.remove(p.getUniqueId());
            p.eject();
            p.teleport(stand.getLocation().clone().add(0.0, 1.7, 0.0));
            stand.remove();
        }
    }
}
