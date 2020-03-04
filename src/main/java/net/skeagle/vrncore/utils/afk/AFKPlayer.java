package net.skeagle.vrncore.utils.afk;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class AFKPlayer {

    private final Player player;
    private final UUID uuid;
    private int secondsPlayed;
    @Setter
    private MouseLocation lastMouseLocation;
    @Setter
    private int secondsAFK;

    public AFKPlayer(final Player player, final UUID uuid) {
        this.secondsAFK = 0;
        this.player = player;
        this.uuid = uuid;
    }

    public void setSecondsPlayed(final int secondsPlayed) {
        if (this.secondsPlayed == -1) {
            return;
        }
        this.secondsPlayed = secondsPlayed;
    }
}
