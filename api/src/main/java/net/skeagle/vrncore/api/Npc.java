package net.skeagle.vrncore.api;

import org.bukkit.entity.Player;

public interface Npc {

    default void updateForPlayer(Player player) {
        this.updateForPlayer(player, null, null);
    }

    void updateForPlayer(Player player, String skinTexture, String skinSignature);

    void removeForPlayer(Player player);
}
