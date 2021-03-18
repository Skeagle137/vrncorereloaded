package net.skeagle.vrncore.event;

import net.skeagle.vrncore.settings.Settings;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.mineacademy.fo.RandomUtil;

import java.util.ArrayList;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

public class ServerListListener implements Listener {

    @EventHandler
    public void onServerListPing(final ServerListPingEvent e) {
        if (!Settings.Motd.ENABLED) return;
        final String message = RandomUtil.nextItem(new ArrayList<>(Settings.Motd.MESSAGES));
        if (Settings.Motd.FIRST_STATIC) {
            e.setMotd(color(Settings.Motd.FIRST_STATIC_TEXT + "\n" + message));
            return;
        }
        e.setMotd(color(message));

    }
}