package net.skeagle.vrncore.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_18_R1.CraftServer;
import org.bukkit.craftbukkit.v1_18_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Npc {
    private final String name;
    private final String display;
    private final Location location;
    private final Skin skin;
    private final boolean rotateHead;
    private int entityId;

    public Npc(final String name, final String display, final Location location, final Skin skin, final boolean rotateHead) {
        this.name = name;
        this.display = display;
        this.location = location;
        this.skin = skin;
        this.rotateHead = rotateHead;
    }

    public void createNPCForPlayer(final Player p) {
        final GameProfile profile = new GameProfile(UUID.randomUUID(), (display.equals(name) ? name : display));
        final ServerPlayer npc = new ServerPlayer(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) location.getWorld()).getHandle(), profile);
        entityId = npc.getId();
        npc.setXRot(location.getYaw());
        npc.setYRot(location.getPitch());
        npc.setPos(location.getX(), location.getY(), location.getZ());
        sendNPCPackets(p, npc);
    }

    public void sendNPCPackets(final Player p, final ServerPlayer npc) {
        final ServerPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, npc));
        if (skin != null) {
            npc.getGameProfile().getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
            final SynchedEntityData data = npc.getEntityData();
            data.set(new EntityDataAccessor<>(17, EntityDataSerializers.BYTE), (byte) 127);
            ((CraftPlayer) p).getHandle().connection.send(new ClientboundSetEntityDataPacket(entityId, data, true));
        }
        nmsPlayer.connection.send(new ClientboundAddPlayerPacket(npc));
        nmsPlayer.connection.send(new ClientboundRotateHeadPacket(npc, (byte) (location.getYaw() * 256 / 360)));
        nmsPlayer.connection.send(new ClientboundMoveEntityPacket.Rot(npc.getId(), (byte) (location.getYaw() * 256 / 360), (byte) (location.getPitch() * 256 / 360), true));
        Task.syncDelayed(() -> nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, npc)), 30);
    }

    public void delete() {
        final ClientboundRemoveEntitiesPacket destroy = new ClientboundRemoveEntitiesPacket(entityId);
        Bukkit.getOnlinePlayers().forEach(pl -> ((CraftPlayer) pl).getHandle().connection.send(destroy));
    }

    public void save() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM npc WHERE name = (?)", name);
            db.execute("INSERT INTO npc (name, display, location, skin, rotatehead) VALUES (?, ?, ?, ?, ?)",
                    name, display, VRNUtil.LocationSerialization.serialize(location), skin != null ? skin.serialize() : null, rotateHead);
        });
    }

    public String getName() {
        return name;
    }

    public String getDisplay() {
        return display;
    }

    public Location getLocation() {
        return location;
    }

    public Skin getSkin() {
        return skin;
    }

    public boolean isRotateHead() {
        return rotateHead;
    }

    public int getEntityId() {
        return entityId;
    }
}
