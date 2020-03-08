package net.skeagle.vrncore.utils.afk;

import lombok.Getter;

import java.util.HashMap;
import java.util.UUID;

public class AFKManager {
    @Getter
    private final HashMap<UUID, UpdatePlayer> onlinePlayers = new HashMap<>();

    public void addAFKPlayer(final UpdatePlayer afkPlayer) {
        this.onlinePlayers.put(afkPlayer.getUuid(), afkPlayer);
    }

    public void removeAFKPlayer(final UpdatePlayer afkPlayer) {
        this.onlinePlayers.remove(afkPlayer.getUuid());
    }
}
