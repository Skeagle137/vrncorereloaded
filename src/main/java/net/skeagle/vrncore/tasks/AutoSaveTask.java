package net.skeagle.vrncore.tasks;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.player.VRNPlayer;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSaveTask extends BukkitRunnable {

    public AutoSaveTask(VRNcore vrn) {
        runTaskTimerAsynchronously(vrn, 20 * (60 * 10), 20 * (60 * 10));
    }

    @Override
    public void run() {
        VRNPlayer p;
        for (Player pl : Bukkit.getOnlinePlayers()) {
            p = new VRNPlayer(pl);
            p.save();
        }
        //homes.saveAll();
        //warps.saveAll();
    }
}
