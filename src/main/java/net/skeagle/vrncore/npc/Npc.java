package net.skeagle.vrncore.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Npc {

    private final int id;
    private final String name;
    private final String display;
    private final Location location;
    private final Skin skin;
    private final boolean rotateHead;
    private int entityId;

    public Npc(final int id, final String name, final String display, final Location location, final Skin skin, final boolean rotateHead) {
        this.id = id;
        this.name = name;
        this.display = display;
        this.location = location;
        this.skin = skin;
        this.rotateHead = rotateHead;
    }

    public void createNPCForPlayer(final Player p) {
        final MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer ws = ((CraftWorld) location.getWorld()).getHandle();
        final GameProfile gp = new GameProfile(UUID.randomUUID(), (display.equals(name) ? name : display));
        if (skin != null)
            gp.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        final EntityPlayer npc = new EntityPlayer(ms, ws, gp, new PlayerInteractManager(ws));
        npc.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
        final DataWatcher watcher = npc.getDataWatcher();
        watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        Task.syncDelayed(() ->
                ((CraftPlayer) p).getHandle().playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), watcher, true)));
        sendNPCPackets(p, npc);
    }

    public void sendNPCPackets(final Player p, final EntityPlayer npc) {
        final EntityPlayer nmsPlayer = ((CraftPlayer) p).getHandle();
        nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        nmsPlayer.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        nmsPlayer.playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        Task.syncDelayed(() ->
                nmsPlayer.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)));
    }

    public void delete() {
        final PacketPlayOutEntityDestroy destroy = new PacketPlayOutEntityDestroy(entityId);
        for (final Player pl : Bukkit.getOnlinePlayers())
            ((CraftPlayer) pl).getHandle().playerConnection.sendPacket(destroy);
    }

    public void save() {
        final SQLHelper db = VRNcore.getInstance().getDB();
        Task.asyncDelayed(() -> {
            db.execute("DELETE FROM npc WHERE id = (?)", getId());
            db.execute("INSERT INTO npc (id, name, display, location, skin, rotatehead) VALUES (?, ?, ?, ?, ?, ?)",
                    id, name, display, VRNUtil.LocationSerialization.serialize(location), skin.serialize(), rotateHead);
        });
    }

    public int getId() {
        return id;
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
