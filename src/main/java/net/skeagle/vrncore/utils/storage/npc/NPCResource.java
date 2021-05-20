package net.skeagle.vrncore.utils.storage.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R3.*;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.util.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R3.CraftServer;
import org.bukkit.craftbukkit.v1_16_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class NPCResource {

    private static final NPCResource instance = new NPCResource();

    public static NPCResource getInstance() {
        return instance;
    }

    private final List<NPCManager> manData = new ArrayList<>();

    public boolean addNewNPC(final String name, final Player p) {
        for (final NPCManager man : manData)
            if (man.getNPCName().equals(name))
                return false;

        final NPCManager man = new NPCManager(name);
        man.save();
        manData.add(man);
        man.setName(name);
        man.setDisplay(name);
        man.setLoc(p.getLocation());
        man.setSkin(name);
        man.setRotatehead(false);
        return true;
    }

    public NPCManager getNPC(final String name) {
        for (final NPCManager man : manData)
            if (man.getNPCName().equals(name))
                return man;

        return null;
    }

    public boolean delNPC(final String name) {
        for (final NPCManager man : manData)
            if (man.getNPCName().equals(name)) {
                man.delete();
                manData.remove(man);
                return true;
            }
        return false;
    }

    public void LoadNPCsForPlayer(final Player p) {
        for (final NPCManager man : manData)
            makeNPC(p, man);
    }

    public void makeNPC(final Player p, final NPCManager man) {
        final Location loc = man.getLoc();
        if (loc.getWorld() == null) return;
        final MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer ws = ((CraftWorld) loc.getWorld()).getHandle();
        final GameProfile gp = new GameProfile(UUID.randomUUID(), (man.getDisplay() == null ? man.getName() : man.getDisplay()));
        final SkinUtil skin = man.getSkin() != null ? man.getSkin() : new SkinUtil(man.getName());
        gp.getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        final EntityPlayer npc = new EntityPlayer(ms, ws, gp, new PlayerInteractManager(ws));
        npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
        final DataWatcher watcher = npc.getDataWatcher();
        watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        Bukkit.getScheduler().runTask(VRNcore.getInstance(), () ->
                ep.playerConnection.sendPacket(new PacketPlayOutEntityMetadata(npc.getId(), watcher, true)));
        sendNPCPackets(p, npc);
    }

    public void sendNPCPackets(final Player p, final EntityPlayer npc) {
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        ep.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        ep.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        ep.playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
        Bukkit.getScheduler().runTask(VRNcore.getInstance(), () ->
                ep.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, npc)));
    }

    public void loadAllNPCs() {
        manData.clear();
        for (final File file : FileUtil.getFiles("npcs", "yml")) {
            final NPCManager man = new NPCManager(file.getName().replace(".yml", ""));
            manData.add(man);
        }
    }
}