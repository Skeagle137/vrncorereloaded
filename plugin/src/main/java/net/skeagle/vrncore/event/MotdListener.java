package net.skeagle.vrncore.event;

import net.skeagle.vrncommands.BukkitUtils;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrnlib.misc.TextResource;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.Plugin;

import java.util.List;
import java.util.Random;

public class MotdListener implements Listener {

    private final Plugin plugin;
    private Random random;
    private List<String> motds;

    public MotdListener(Plugin plugin) {
        this.plugin = plugin;
        this.loadMotds();
    }

    public void loadMotds() {
        if (Settings.randomMotd) {
            random = new Random();
            motds = TextResource.load(plugin, "motds.txt");
        }
    }

    @EventHandler
    public void onList(ServerListPingEvent e) {
        String secondLine = motds != null ? BukkitUtils.color("&9" + motds.get(random.nextInt(motds.size()))) : BukkitUtils.color(Settings.secondLineText);
        e.setMotd(Settings.firstLineShown ? BukkitUtils.color(Settings.firstLineText + "\n" + secondLine) : BukkitUtils.color(secondLine));
    }
}
