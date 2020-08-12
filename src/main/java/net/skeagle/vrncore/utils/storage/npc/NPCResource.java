package net.skeagle.vrncore.utils.storage.npc;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.minecraft.server.v1_16_R2.*;
import net.skeagle.vrncore.utils.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.FileUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class NPCResource {

    @Getter
    private static final NPCResource instance = new NPCResource();

    @Getter
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
        man.setSkin("Skeagle_");
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

    public void updateNPCsForPlayer(final Player p) {
        for (final NPCManager man : manData) {
            final MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
            final WorldServer ws = ((CraftWorld) Bukkit.getWorld(p.getWorld().getName())).getHandle();
            final GameProfile gp = new GameProfile(UUID.randomUUID(), (man.getDisplay() == null ? man.getName() : man.getDisplay()));
            final EntityPlayer npc = new EntityPlayer(ms, ws, gp, new PlayerInteractManager(ws));
            final Location loc = man.getLoc();
            npc.setLocation(loc.getX(), loc.getY(), loc.getZ(), loc.getYaw(), loc.getPitch());
            if (man.getSkin() != null) {
                final SkinUtil skin = man.getSkin();
                npc.getProfile().getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
            }
            updateNPC(p, npc);
        }
    }

    public void updateNPC(final Player p, final EntityPlayer npc) {
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        ep.playerConnection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc));
        ep.playerConnection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc));
        ep.playerConnection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360)));
    }

    public void loadAllNPCs() {
        manData.clear();
        for (final File file : FileUtil.getFiles("npcs", "yml")) {
            final NPCManager man = new NPCManager(file.getName().replace(".yml", ""));
            manData.add(man);
        }
    }
}