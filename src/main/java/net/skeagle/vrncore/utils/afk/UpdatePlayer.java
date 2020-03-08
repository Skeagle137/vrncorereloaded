package net.skeagle.vrncore.utils.afk;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class UpdatePlayer {

    private final Player player;
    private final UUID uuid;
    private int timeAfk;
    @Setter
    private MouseLocation lastMouseLocation;
    @Setter
    private int secondsAFK;

    public UpdatePlayer(final Player player, final UUID uuid) {
        this.secondsAFK = 0;
        this.player = player;
        this.uuid = uuid;
    }

    public void setTimeAfk(final int secondsPlayed) {
        if (this.timeAfk == -1) {
            return;
        }
        this.timeAfk = secondsPlayed;
    }
}
