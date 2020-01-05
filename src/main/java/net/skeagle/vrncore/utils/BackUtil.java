package net.skeagle.vrncore.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BackUtil {

    private static BackUtil back;
    private final Map<UUID, Location> backLoc = new HashMap<>();

    private BackUtil() {

    }

    public static BackUtil getBack() {
        if (back == null) {
            back = new BackUtil();
        }
        return back;
    }

    public void setBackLoc(UUID id, Location loc) {
        this.backLoc.remove(id);
        this.backLoc.put(id, loc);
    }

    public boolean hasBackLoc(UUID id) {
        return this.backLoc.containsKey(id);
    }

    public void teleToBackLoc(UUID id, Player targetLoc) {
        Bukkit.getPlayer(id).teleport(backLoc.get(targetLoc.getUniqueId()));
    }
}
