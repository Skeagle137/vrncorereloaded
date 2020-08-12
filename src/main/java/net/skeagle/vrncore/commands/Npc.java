package net.skeagle.vrncore.commands;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.server.v1_16_R2.*;
import net.skeagle.vrncore.utils.SkinUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.npc.NPCResource;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_16_R2.CraftServer;
import org.bukkit.craftbukkit.v1_16_R2.CraftWorld;
import org.bukkit.craftbukkit.v1_16_R2.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Npc extends SimpleCommand {

    public Npc() {
        super("npc");
        setMinArguments(1);
        setUsage("<create|delete> <name>");
        setDescription("Spawn an NPC where you are looking.");
        setPermission("vrn.spawnnpc");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        if (args.length < 1) {
            say(getPlayer(), "&cYou must specify whether you want to create or remove an npc.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "create":
                if (args.length == 1) {
                    say(getPlayer(), "&cYou must provide a name for the npc.");
                    return;
                }
                if (NPCResource.getInstance().addNewNPC(args[1], getPlayer())) {
                    makeNPC();
                    say(getPlayer(), "&7NPC successfully created.");
                    return;
                }
                say(getPlayer(), "&cThat npc name already exists.");
                return;
            case "remove":
                if (NPCResource.getInstance().delNPC(args[1])) {
                    say(getPlayer(), "&cThat npc name already exists.");
                    return;
                }
                say(getPlayer(), "&7NPC successfully removed.");
                return;
            default:
                say(getPlayer(), "&cUnkown option, use create or remove.");
        }

    }

    private void makeNPC() {
        final MinecraftServer ms = ((CraftServer) Bukkit.getServer()).getServer();
        final WorldServer ws = ((CraftWorld) Bukkit.getWorld(getPlayer().getWorld().getName())).getHandle();
        final GameProfile gp = new GameProfile(UUID.randomUUID(), args[1]);
        final EntityPlayer npc = new EntityPlayer(ms, ws, gp, new PlayerInteractManager(ws));
        npc.setLocation(getPlayer().getLocation().getX(), getPlayer().getLocation().getY(),
                getPlayer().getLocation().getZ(), getPlayer().getLocation().getYaw(), getPlayer().getLocation().getPitch());
        final SkinUtil skin = new SkinUtil(getPlayer().getName());
        npc.getProfile().getProperties().put("textures", new Property("textures", skin.getTexture(), skin.getSignature()));
        final DataWatcher watcher = npc.getDataWatcher();
        for (final Player p : Bukkit.getOnlinePlayers()) {
            NPCResource.getInstance().updateNPC(p, npc);
        }
        watcher.set(new DataWatcherObject<>(16, DataWatcherRegistry.a), (byte) 127);
        final PacketPlayOutEntityMetadata metadata = new PacketPlayOutEntityMetadata(npc.getId(), watcher, true);
        final EntityPlayer ep = ((CraftPlayer) getPlayer()).getHandle();
        ep.playerConnection.sendPacket(metadata);
    }
}
