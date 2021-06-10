package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrnlib.commandmanager.TextResource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class MotdListener implements Listener {

    private Random random;
    private List<String> motds;

    public MotdListener(VRNcore plugin) {
        if (Settings.Motd.randomMotd) {
            random = new Random();
            motds = TextResource.load(plugin, "motds.txt");
        }
    }

    @EventHandler
    public void onList(ServerListPingEvent e) {
        String secondLine = motds != null ? color("&9" + motds.get(random.nextInt(motds.size()))) : color(Settings.Motd.secondLineText);
        e.setMotd(Settings.Motd.firstLineShown ? color(Settings.Motd.firstLineText + "\n" + secondLine) : color(secondLine));
    }
}
