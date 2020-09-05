package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Top extends SimpleCommand {

    public Top() {
        super("top");
        setDescription("Teleport to the highest block above you.");
        setPermission("vrn.top");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        final int Y;
        final Block b = VRNUtil.getBlockExact(getPlayer().getLocation());
        if (b != null)
            Y = getPlayer().getWorld().getHighestBlockYAt(b.getLocation());
        else
            Y = getPlayer().getWorld().getHighestBlockYAt(getPlayer().getLocation());
        say(getPlayer(), "Teleporting...");
        getPlayer().teleport(new Location(getPlayer().getWorld(), getPlayer().getLocation().getX(), Y + 1, getPlayer().getLocation().getZ(),
                getPlayer().getLocation().getYaw(), getPlayer().getLocation().getPitch()));
    }
}
