package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class FunCommands {

    @CommandHook("demo")
    public void onDemo(final CommandSender sender, final Player target) {
        final CraftPlayer craftPlayer = (CraftPlayer) target;
        final PacketPlayOutGameStateChange gamestate = new PacketPlayOutGameStateChange(new PacketPlayOutGameStateChange.a(5), 0);
        craftPlayer.getHandle().playerConnection.sendPacket(gamestate);
        say(sender, "Now showing &a" + craftPlayer.getName() + " &7the demo menu.");
    }

    @CommandHook("sudo")
    public void onSudo(final CommandSender sender, final Player target, final String command) {
        if (!command.startsWith("/")) {
            target.chat(command);
            return;
        }
        Bukkit.dispatchCommand(target, command.substring(1));
        say(sender, "Made target execute command: &a" + command);
    }

    @CommandHook("hallucinate")
    public void onHallucinate(final Player player, final Player target) {
        final Player halluPlayer = target != null && target != player ? target : player;
        final EntityPlayer entityPlayer = ((CraftPlayer) halluPlayer).getHandle();
        final EntityEnderDragon dragon = new EntityEnderDragon(EntityTypes.ENDER_DRAGON, entityPlayer.world);
        dragon.u(entityPlayer); //shorthand setPositionRotation
        dragon.setInvulnerable(true);
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutSpawnEntityLiving(dragon));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(dragon.getId(), dragon.getDataWatcher(), true));
        say(halluPlayer, halluPlayer == player ? "Sent yourself a hallucination. Why you would ever want this is beyond me." :
                "Sent &a" + halluPlayer.getName() + "&7 a hallucination.");
    }

    @CommandHook("push")
    public void onPush(final CommandSender sender, final Player target, final int multiplier) {
        double d = 0.5 * multiplier;
        if (d > 10)
            d = 10;
        if (d < 1)
            d = 0.2;
        target.setVelocity(target.getLocation().getDirection().multiply(-3.5 * d).setY(1.5 * d));
        say(sender, "You pushed &a" + target.getName() + "!");
    }


}
