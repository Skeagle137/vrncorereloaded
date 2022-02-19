package net.skeagle.vrncore.hook;

import de.myzelyam.api.vanish.VanishAPI;
import org.bukkit.entity.Player;

public class SuperVanishHook {

    public boolean canSee(Player viewed, Player viewer) {
        return VanishAPI.canSee(viewed, viewer);
    }
}
