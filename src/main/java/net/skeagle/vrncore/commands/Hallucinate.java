package net.skeagle.vrncore.commands;

import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Hallucinate extends SimpleCommand {

    public Hallucinate() {
        super("hallucinate|hallu");
        setDescription("Make another player see hallucinations >:)");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final Player p = getPlayer();
            hasPerm("vrn.hallucinate.self");
            sendHallucination(p);
            say(p, "Sent yourself a hallucination.");
            return;
        }
        hasPerm("vrn.hallucinate.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        sendHallucination(a);
        say(getSender(), "Sent &a" + a.getName() + "&7 a hallucination.");
    }

    private void sendHallucination(final Player p) {
        final WorldServer world = ((CraftWorld) p.getWorld()).getHandle();
        final EntityEnderDragon dragon = new EntityEnderDragon(EntityTypes.ENDER_DRAGON, world);
        dragon.setPositionRotation(p.getLocation().getX(), p.getLocation().getY(), p.getLocation().getZ(), p.getLocation().getYaw(), p.getLocation().getPitch());
        dragon.setInvulnerable(true);
        final PacketPlayOutSpawnEntityLiving spawnEntityLiving = new PacketPlayOutSpawnEntityLiving(dragon);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(spawnEntityLiving);
        final PacketPlayOutEntityMetadata entityMetadata = new PacketPlayOutEntityMetadata(dragon.getId(), dragon.getDataWatcher(), false);
        ((CraftPlayer) p).getHandle().playerConnection.sendPacket(entityMetadata);
    }
}
