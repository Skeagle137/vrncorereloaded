package net.skeagle.vrncore.commands;

import com.mojang.authlib.properties.Property;
import net.minecraft.network.protocol.game.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.level.biome.BiomeManager;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.SkinUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_19_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class FunCommands {

    private final Map<UUID, Property> skinCache;

    public FunCommands() {
        skinCache = new HashMap<>();
    }

    @CommandHook("demo")
    public void onDemo(final CommandSender sender, final Player target) {
        ((CraftPlayer) target).getHandle().connection.send(new ClientboundGameEventPacket(ClientboundGameEventPacket.DEMO_EVENT, 0));
        say(sender, "Now showing &a" + target.getName() + " &7the demo menu.");
    }

    @CommandHook("sudo")
    public void onSudo(final CommandSender sender, final Player target, final String command) {
        if (!command.startsWith("/")) {
            target.chat(command);
            return;
        }
        Bukkit.dispatchCommand(target, command.substring(1));
        say(sender, "Made " + target.getName() + " execute command: &a" + command);
    }

    @CommandHook("hallucinate")
    public void onHallucinate(final CommandSender sender, final Player target) {
        final ServerPlayer entityPlayer = ((CraftPlayer) target).getHandle();
        final EnderDragon dragon = new EnderDragon(EntityType.ENDER_DRAGON, entityPlayer.level);
        dragon.copyPosition(entityPlayer);
        dragon.setInvulnerable(true);
        entityPlayer.connection.send(new ClientboundAddEntityPacket(dragon));
        say(target, target == sender ? "Sent yourself a hallucination. Why you would ever want this is beyond me." :
                "Sent &a" + target.getName() + "&7 a hallucination.");
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
        if (name == null || name.equalsIgnoreCase(player.getName())) {
            if (!skinCache.containsKey(player.getUniqueId())) {
                say(player, "&cYou do not currently have a different skin active.");
                return;
            }
            replaceSkin(player, null);
            say(player, "&aYour skin has been reset.");
        }
        final Skin skin = SkinUtil.getSkin(name);
        if (skin == null) {
            say(player, "&cThe skin could not be retrieved. Likely there is no player with this name.");
            return;
        }
        replaceSkin(player, skin);
        say(player, "&aYour skin has been changed successfully. To reset it, run /skin.");
    }

    private void replaceSkin(Player player, Skin skin) {
        final ServerPlayer nmsPlayer = ((CraftPlayer) player).getHandle();
        final Property property = nmsPlayer.getGameProfile().getProperties().get("textures").iterator().next();

        if (!skinCache.containsKey(player.getUniqueId())) {
            skinCache.put(player.getUniqueId(), property);
        }
        nmsPlayer.getGameProfile().getProperties().remove("textures", property);
        Property newProperty = skin != null ? new Property("textures", skin.getTexture(), skin.getSignature()) : skinCache.remove(player.getUniqueId());
        nmsPlayer.getGameProfile().getProperties().put("textures", newProperty);

        final Location loc = player.getLocation().clone();
        final ServerLevel level = (ServerLevel) nmsPlayer.level;
        nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, nmsPlayer));
        nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, nmsPlayer));
        nmsPlayer.connection.send(new ClientboundRespawnPacket(level.dimensionTypeId(), level.dimension(), BiomeManager.obfuscateSeed(level.getSeed()),
                nmsPlayer.gameMode.getGameModeForPlayer(), nmsPlayer.gameMode.getPreviousGameModeForPlayer(),
                level.isDebug(), level.isFlat(), true, nmsPlayer.getLastDeathLocation()));
        nmsPlayer.connection.send(new ClientboundPlayerPositionPacket(loc.getX(), loc.getY(), loc.getZ(), nmsPlayer.getYRot(),
                nmsPlayer.getXRot(), Collections.emptySet(), -1337, false));
        nmsPlayer.getBukkitEntity().updateScaledHealth(true);

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
    }
}
