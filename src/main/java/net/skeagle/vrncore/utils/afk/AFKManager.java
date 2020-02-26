package net.skeagle.vrncore.utils.afk;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class AFKManager {
    @Getter
    private HashMap<UUID, AFKPlayer> onlinePlayers = new HashMap<>();

    public void addAFKPlayer(final AFKPlayer afkPlayer) {
        this.onlinePlayers.put(afkPlayer.getUuid(), afkPlayer);
    }

    public void removeAFKPlayer(final AFKPlayer afkPlayer) {
        this.onlinePlayers.remove(afkPlayer.getUuid());
    }
}
