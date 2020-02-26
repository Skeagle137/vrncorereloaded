package net.skeagle.vrncore.utils.afk;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

import java.util.UUID;

@Getter
public class AFKPlayer {

    private Player player;
    private UUID uuid;
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

    public String timeToMessage(int time) {
        long days = time / 86400L;
        long hours = (time % 86400L) / 3600L;
        long minutes = (time % 86400L % 3600L) / 60L;
        long seconds = time % 86400L % 3600L % 60L;
        return ((days != 0) ? days + " " + timeGrammarCheck("days", time) + ", " : "") +
                ((hours != 0) ? hours + " " + timeGrammarCheck("hours", time) + ", " : "") +
                ((minutes != 0) ? minutes + " " + timeGrammarCheck("minutes", time) + ", " : "") +
                ((seconds != 0) ? seconds + " " + timeGrammarCheck("seconds", time) : "");
    }

    private String timeGrammarCheck(String s, int i) {
        if (i != 1) {
            return s;
        }
        return s.substring(s.length() - 1);
    }

    public void setSecondsPlayed(final int secondsPlayed) {
        if (this.secondsPlayed == -1) {
            return;
        }
        this.secondsPlayed = secondsPlayed;
    }
}
