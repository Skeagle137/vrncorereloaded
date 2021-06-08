package net.skeagle.vrncore.event;

import net.skeagle.vrncore.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class MotdListener implements Listener {

    private final Random random;
    private final List<String> motds;

    public MotdListener(final List<String> motds) {
        random = new Random();
        this.motds = motds;
    }

    @EventHandler
    public void onList(final ServerListPingEvent e) {
        final String message = color("&9" + motds.get(random.nextInt(motds.size())));
        e.setMotd(Settings.Motd.FIRST_STATIC ? color(Settings.Motd.FIRST_STATIC_TEXT + "\n" + message) : color(message));
    }
}
