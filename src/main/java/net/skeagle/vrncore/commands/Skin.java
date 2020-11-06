package net.skeagle.vrncore.commands;

/*
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
 */

import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class Skin extends SimpleCommand {

    public Skin() {
        super("skin");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Changes your skin to the specified player's skin (including offline players).");
        setPermission("vrn.skin");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        setSkin(getPlayer(), args[0]);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            pl.hidePlayer(VRNcore.getInstance(), getPlayer());
            pl.showPlayer(VRNcore.getInstance(), getPlayer());
        }
    }

    private void setSkin(final Player p, final String s) {
        final String[] skin = VRNUtil.getSkin(s);
        if (skin == null) {
            say(getPlayer(), "&cThe skin could not be retrieved. Likely there is no player with this name.");
            return;
        }
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        final Property property = ep.getProfile().getProperties().get("textures").iterator().next();
        ep.getProfile().getProperties().remove("textures", property);
        ep.getProfile().getProperties().put("textures", new Property("textures", skin[0], skin[1]));
        reloadSkin(p);

        p.updateInventory();
        final PlayerInventory inventory = p.getInventory();
        inventory.setHeldItemSlot(inventory.getHeldItemSlot());
        final float experience = p.getExp();
        final int totalExperience = p.getTotalExperience();
        p.setExp(experience);
        p.setTotalExperience(totalExperience);
        p.getInventory().setItemInMainHand(p.getInventory().getItemInMainHand());
        p.getInventory().setItemInOffHand(p.getInventory().getItemInOffHand());
        p.setWalkSpeed(p.getWalkSpeed());
        say(p, "&aYour skin has been changed successfully.");
    }

    //this is necessary to see the changes applied to yourself
    private void reloadSkin(final Player p) {
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        final Location loc = p.getLocation().clone();
        final WorldServer ws = ep.getWorldServer();
        final PacketPlayOutRespawn respawn = new PacketPlayOutRespawn(ws.getDimensionManager(), ws.getDimensionKey(), ws.getSeed(),
                ep.playerInteractManager.getGameMode(), ep.playerInteractManager.getGameMode(), ws.isDebugWorld(), ws.isFlatWorld(), true);
        final PacketPlayOutPosition teleport = new PacketPlayOutPosition(loc.getX(), loc.getY(), loc.getZ(),
                p.getLocation().getYaw(), p.getLocation().getPitch(), Collections.emptySet(), -1337);
        ep.playerConnection.sendPacket(removeInfo);
        ep.playerConnection.sendPacket(addInfo);
        ep.playerConnection.sendPacket(respawn);
        ep.playerConnection.sendPacket(teleport);
        ((CraftPlayer) p).updateScaledHealth(true);
    }
}