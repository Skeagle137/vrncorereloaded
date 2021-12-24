package net.skeagle.vrncore.event;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrnlib.misc.TextResource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import java.util.List;
import java.util.Random;

import static net.skeagle.vrncommands.BukkitUtils.color;

public class MotdListener implements Listener {

    private Random random;
    private List<String> motds;

    public MotdListener(VRNcore plugin) {
        if (Settings.randomMotd) {
            random = new Random();
            motds = TextResource.load(plugin, "motds.txt");
        }
    }

    @EventHandler
    public void onList(ServerListPingEvent e) {
        String secondLine = motds != null ? color("&9" + motds.get(random.nextInt(motds.size()))) : color(Settings.secondLineText);
        e.setMotd(Settings.firstLineShown ? color(Settings.firstLineText + "\n" + secondLine) : color(secondLine));
    }
}
