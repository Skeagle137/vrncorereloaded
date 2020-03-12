package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_15_R1.PacketPlayOutGameStateChange;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Demotroll extends SimpleCommand {

    public Demotroll() {
        super("demo");
        setMinArguments(1);
        setUsage("<player>");
        setPermission("vrn.demo");
        setDescription("Shows the demo welcome screen to a player, used for trolls");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final CraftPlayer cp = (CraftPlayer) a;
        final PacketPlayOutGameStateChange gamestate = new PacketPlayOutGameStateChange(5, 0);
        cp.getHandle().playerConnection.sendPacket(gamestate);
        say(getSender(), "Now showing &a" + a.getName() + " &7the demo menu.");
    }
}
