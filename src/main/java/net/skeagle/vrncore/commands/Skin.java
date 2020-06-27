package net.skeagle.vrncore.commands;

/*
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
 */

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;


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
        //setSkin(getPlayer(), args[0]);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            pl.hidePlayer(VRNcore.getInstance(), getPlayer());
            pl.showPlayer(VRNcore.getInstance(), getPlayer());
        }
    }
    /*

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
        final Location location = p.getLocation().clone();
        final EnumWrappers.NativeGameMode gamemode = EnumWrappers.NativeGameMode.fromBukkit(p.getGameMode());
        final PacketContainer respawn = new PacketContainer(PacketType.Play.Server.RESPAWN);
        respawn.getDimensions().write(0, ep.dimension.getDimensionID());
        respawn.getWorldTypeModifier().write(0, p.getWorld().getWorldType());
        respawn.getGameModes().write(0, gamemode);
        final PacketContainer teleport = new PacketContainer(PacketType.Play.Server.POSITION);
        teleport.getModifier().writeDefaults();
        teleport.getDoubles().write(0, location.getX());
        teleport.getDoubles().write(1, location.getY());
        teleport.getDoubles().write(2, location.getZ());
        teleport.getFloat().write(0, location.getYaw());
        teleport.getFloat().write(1, location.getPitch());
        teleport.getIntegers().writeSafely(0, (-1337));
        ep.playerConnection.sendPacket(removeInfo);
        ep.playerConnection.sendPacket(addInfo);
        sendPackets(p, respawn, teleport);
        final PacketPlayOutUpdateHealth health = new PacketPlayOutUpdateHealth((float) p.getHealth(), p.getFoodLevel(), p.getSaturation());
        ep.playerConnection.sendPacket(health);
    }

    private void sendPackets(final Player p, final PacketContainer... packets) {
        try {
            for (final PacketContainer packet : packets) {
                ProtocolLibrary.getProtocolManager().sendServerPacket(p, packet);
            }
        } catch (final InvocationTargetException ex) {
            Common.log("&cError sending skin change packet.");
        }
    }

     */
}