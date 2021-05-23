package net.skeagle.vrncore.commands;

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.SkinUtil;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;

import static net.skeagle.vrncore.utils.VRNUtil.say;

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

    @CommandHook("skin")
    public void onSkin(final Player player, final String name) {
        final Skin skin = SkinUtil.getSkin(name);
        if (skin == null) {
            say(player, "&cThe skin could not be retrieved. Likely there is no player with this name.");
            return;
        }
        final EntityPlayer ep = ((CraftPlayer) player).getHandle();
        final Property property = ep.getProfile().getProperties().get("textures").iterator().next();
        ep.getProfile().getProperties().remove("textures", property);
        ep.getProfile().getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));

        final EntityPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final Location loc = player.getLocation().clone();
        final WorldServer ws = entityPlayer.getWorldServer();
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, entityPlayer));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, entityPlayer));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutRespawn(ws.getDimensionManager(), ws.getDimensionKey(), ws.getSeed(),
                entityPlayer.playerInteractManager.getGameMode(), entityPlayer.playerInteractManager.getGameMode(), ws.isDebugWorld(), ws.isFlatWorld(), true));
        entityPlayer.playerConnection.sendPacket(new PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(),
                player.getLocation().getYaw(), player.getLocation().getPitch(), Collections.emptySet(), -1337));
        ((CraftPlayer) player).updateScaledHealth(true);

        player.updateInventory();
        final PlayerInventory inventory = player.getInventory();
        inventory.setHeldItemSlot(inventory.getHeldItemSlot());
        final float experience = player.getExp();
        final int totalExperience = player.getTotalExperience();
        player.setExp(experience);
        player.setTotalExperience(totalExperience);
        player.getInventory().setItemInMainHand(player.getInventory().getItemInMainHand());
        player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
        player.setWalkSpeed(player.getWalkSpeed());
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            pl.hidePlayer(VRNcore.getInstance(), player);
            pl.showPlayer(VRNcore.getInstance(), player);
        }
        say(player, "&aYour skin has been changed successfully.");
    }
}
