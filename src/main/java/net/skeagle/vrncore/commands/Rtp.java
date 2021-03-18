package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Random;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Rtp extends SimpleCommand {

    public Rtp() {
        super("rtp|wild");
        setDescription("Teleports to a random area from the spawn.");
        setPermission("vrn.rtp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        Common.runLater(() -> rtp(getPlayer()));
    }

    private void rtp(final Player p) {
        say(p, "&aSearching for a destination.");
        final Random r = new Random();
        final double x;
        final double z;
        final double origin_x;
        final double origin_z;
        final int min_x;
        final int max_x;
        final int min_z;
        final int max_z;
        min_x = Settings.Rtp.min_x;
        max_x = Settings.Rtp.max_x;
        min_z = Settings.Rtp.min_z;
        max_z = Settings.Rtp.max_z;
        origin_x = Settings.Rtp.x;
        origin_z = Settings.Rtp.z;
        final Location loc;
        x = r.nextInt(min_x + (max_x - min_x));
        z = r.nextInt(min_z + (max_z - min_z));
        loc = new Location(p.getWorld(), origin_x + x + 0.5, 200, origin_z + z + 0.5, p.getLocation().getYaw(), p.getLocation().getPitch());

        if (p.getWorld().getEnvironment() == World.Environment.NETHER) {
            loc.setY((p.getWorld().getHighestBlockYAt(loc) - 4));
        }
        for (int i = loc.getBlockY(); i > 6; --i) {
            loc.setY(i);
            final Location one_up = loc.clone().add(0, 1, 0);
            final Location one_down = loc.clone().subtract(0, 1, 0);
            final Location two_down = loc.clone().subtract(0, 2, 0);
            if (loc.getBlock().getType().isAir() && one_up.getBlock().getType().isAir() &&
                    checkBlock(one_down.getBlock()) && checkBlock(two_down.getBlock())) {
                p.teleport(loc);
                say(p, "You have been teleported to " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ() + ".");
                return;
            }
        }
        say(p, "&cCould not find a suitable location to teleport to.");
    }

    private boolean checkBlock(final Block b) {
        return b.getType().isSolid() &&
                b.getType() != Material.LAVA &&
                !b.isLiquid() && !b.isPassable();
    }
}
