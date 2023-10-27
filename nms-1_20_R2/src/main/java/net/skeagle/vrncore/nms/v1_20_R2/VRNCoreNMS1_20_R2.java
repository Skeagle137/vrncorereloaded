package net.skeagle.vrncore.nms.v1_20_R2;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ClientInformation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.biome.BiomeManager;
import net.skeagle.vrncore.api.Npc;
import net.skeagle.vrncore.api.VRNCoreNMS;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_20_R2.CraftServer;
import org.bukkit.craftbukkit.v1_20_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_20_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class VRNCoreNMS1_20_R2 implements VRNCoreNMS {

    @Override
    public void showDemoMenu(Player player) {
        ((CraftPlayer) player).getHandle().connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0));
    }

    @Override
    public void showHallucination(Player player) {
        final ServerPlayer entityPlayer = ((CraftPlayer) player).getHandle();
        final EnderDragon dragon = new EnderDragon(EntityType.ENDER_DRAGON, entityPlayer.level());
        dragon.copyPosition(entityPlayer);
        dragon.setInvulnerable(true);
        entityPlayer.connection.send(new ClientboundAddEntityPacket(dragon));
    }

    @Override
    public Property getTexturesProperty(Player player) {
        final ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        return nmsPlayer.getGameProfile().getProperties().get("textures").iterator().next();
    }

    @Override
    public void replaceProperty(Player player, String name, Property oldProperty, Property newProperty) {
        final ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        nmsPlayer.getGameProfile().getProperties().remove(name, oldProperty);
        nmsPlayer.getGameProfile().getProperties().put(name, newProperty);
    }

    @Override
    public void reloadSkin(Player player) {
        final Location loc = player.getLocation().clone();
        final ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        final ServerLevel level = (ServerLevel) nmsPlayer.level();
        nmsPlayer.connection.send(new ClientboundPlayerInfoRemovePacket(List.of(player.getUniqueId())));
        nmsPlayer.connection.send(new ClientboundPlayerInfoUpdatePacket(ClientboundPlayerInfoUpdatePacket.Action.ADD_PLAYER, nmsPlayer));
        nmsPlayer.connection.send(new ClientboundRespawnPacket(new CommonPlayerSpawnInfo(level.dimensionTypeId(), level.dimension(), BiomeManager.obfuscateSeed(level.getSeed()),
                nmsPlayer.gameMode.getGameModeForPlayer(), nmsPlayer.gameMode.getPreviousGameModeForPlayer(),
                level.isDebug(), level.isFlat(), nmsPlayer.getLastDeathLocation(), nmsPlayer.getPortalCooldown()), (byte) 3));
        nmsPlayer.connection.send(new ClientboundPlayerPositionPacket(loc.getX(), loc.getY(), loc.getZ(), nmsPlayer.getYRot(),
                nmsPlayer.getXRot(), Collections.emptySet(), -1337));
        nmsPlayer.getBukkitEntity().updateScaledHealth(true);
    }

    @Override
    public Npc createNpc(String name, Location location) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), name);
        final ServerPlayer npc = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) location.getWorld()).getHandle(), profile, ClientInformation.createDefault());
        npc.setXRot(location.getYaw());
        npc.setYRot(location.getPitch());
        npc.setPos(location.getX(), location.getY(), location.getZ());
        return new NMSNpc(profile, npc);
    }
}
