package net.skeagle.vrncore.hook;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;

public class SuperVanishHook {

    public static boolean canSee(Player viewer, Player viewed) {
        return VanishAPI.canSee(viewer, viewed);
    }

    public static boolean isVanished(Player player) {
        return VanishAPI.isInvisible(player);
    }
}
