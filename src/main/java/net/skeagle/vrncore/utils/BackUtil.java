package net.skeagle.vrncore.utils;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public final class BackUtil {
    private final Map<UUID, Location> backLoc = new HashMap<>();

    @Getter
    public static BackUtil instance = new BackUtil();

    public void setBackLoc(final UUID id, final Location loc) {
        this.backLoc.remove(id);
        this.backLoc.put(id, loc);
    }

    public boolean hasBackLoc(final UUID id) {
        return this.backLoc.containsKey(id);
    }

    public void teleToBackLoc(final UUID id, final Player targetLoc) {
        Bukkit.getPlayer(id).teleport(backLoc.get(targetLoc.getUniqueId()));
    }
}
