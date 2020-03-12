package net.skeagle.vrncore.commands;


import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import com.mojang.authlib.properties.PropertyMap;
import net.minecraft.server.v1_15_R1.EntityPlayer;
import net.minecraft.server.v1_15_R1.PacketPlayOutPlayerInfo;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_15_R1.CraftServer;
import org.bukkit.craftbukkit.v1_15_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

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
        try {
            final URL url_0 = new URL("https://api.mojang.com/users/profiles/minecraft/" + s);
            final InputStreamReader reader_0 = new InputStreamReader(url_0.openStream());
            final String uuid = new JsonParser().parse(reader_0).getAsJsonObject().get("id").getAsString();

            final URL url_1 = new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + uuid + "?unsigned=false");
            final InputStreamReader reader_1 = new InputStreamReader(url_1.openStream());
            final JsonObject textureProperty = new JsonParser().parse(reader_1).getAsJsonObject().get("properties").getAsJsonArray().get(0).getAsJsonObject();
            final String texture = textureProperty.get("value").getAsString();
            final String signature = textureProperty.get("signature").getAsString();

            final EntityPlayer ep = ((CraftPlayer) p).getHandle();
            final GameProfile gp = ep.getProfile();
            final PropertyMap pm = gp.getProperties();
            final Property property = pm.get("textures").iterator().next();
            pm.remove("textures", property);
            pm.put("textures", new Property("textures", texture, signature));
            reloadSkin(p);
            say(p, "&aYour skin has been changed successfully.");
        } catch (final IOException e) {
            System.err.println("Could not get skin data from session servers!");
            e.printStackTrace();
        }
    }

    //this is necessary to see the changes applied to yourself
    private void reloadSkin(final Player p) {
        final EntityPlayer ep = ((CraftPlayer) p).getHandle();
        final CraftServer cs = ((CraftServer) Bukkit.getServer());
        final PacketPlayOutPlayerInfo removeInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.REMOVE_PLAYER, ep);
        final PacketPlayOutPlayerInfo addInfo = new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, ep);
        ep.playerConnection.sendPacket(removeInfo);
        ep.playerConnection.sendPacket(addInfo);
        cs.getHandle().moveToWorld(ep, ep.dimension, true, p.getLocation(), true);
    }
}