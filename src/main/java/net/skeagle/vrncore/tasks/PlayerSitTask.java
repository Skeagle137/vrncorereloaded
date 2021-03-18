package net.skeagle.vrncore.tasks;

import net.minecraft.server.v1_16_R3.EntityArmorStand;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.PlayerSitUtil;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftArmorStand;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSitTask extends BukkitRunnable {

    public PlayerSitTask(VRNcore vrn) {
        runTaskTimer(vrn, 0, 1);
    }

    public void run() {
        for (ArmorStand stand : PlayerSitUtil.getSitmap().values()) {
            if (!stand.getPassengers().isEmpty()) {
                EntityArmorStand entitystand = ((CraftArmorStand) stand).getHandle();
                entitystand.yaw = stand.getPassengers().get(0).getLocation().getYaw();
                if (VRNUtil.getBlockExact(stand.getLocation().add(0, 1.7, 0)) == null) {
                    PlayerSitUtil p = PlayerSitUtil.getPlayer((Player)stand.getPassengers().get(0));
                    p.setSitting(false);
                }
            }
        }
    }
}