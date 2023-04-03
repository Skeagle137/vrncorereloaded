package net.skeagle.vrncore.commands;

import com.mojang.authlib.properties.Property;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.api.VRNCoreNMS;
import net.skeagle.vrncore.utils.Skin;
import net.skeagle.vrncore.utils.SkinUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

import java.util.*;

@SuppressWarnings("unused")
public class FunCommands {

    private final VRNCoreNMS api;
    private final Map<UUID, Property> skinCache;

    public FunCommands(VRNCoreNMS api) {
        this.api = api;
        skinCache = new HashMap<>();
    }

    @CommandHook("demo")
    public void onDemo(final CommandSender sender, final Player target) {
        api.showDemoMenu(target);
        VRNUtil.say(sender, "Now showing &a" + target.getName() + " &7the demo menu.");
    }

    @CommandHook("sudo")
    public void onSudo(final CommandSender sender, final Player target, final String command) {
        if (!command.startsWith("/")) {
            target.chat(command);
            return;
        }
        Bukkit.dispatchCommand(target, command.substring(1));
        VRNUtil.say(sender, "Made " + target.getName() + " execute command: &a" + command);
    }

    @CommandHook("hallucinate")
    public void onHallucinate(final CommandSender sender, final Player target) {
        api.showHallucination(target);
        VRNUtil.say(sender, target == sender ? "Sent yourself a hallucination. Why you would ever want this is beyond me." :
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
        VRNUtil.say(sender, "You pushed &a" + target.getName() + "!");
    }

    @CommandHook("skin")
    public void onSkin(final Player player, final String name) {
        if (name == null || name.equalsIgnoreCase(player.getName())) {
            if (!skinCache.containsKey(player.getUniqueId())) {
                VRNUtil.say(player, "&cYou do not currently have a different skin active.");
                return;
            }
            replaceSkin(player, null);
            VRNUtil.say(player, "&aYour skin has been reset.");
            return;
        }
        final Skin skin = SkinUtil.getSkin(name);
        if (skin == null) {
            VRNUtil.say(player, "&cThe skin could not be retrieved. Likely there is no player with this name.");
            return;
        }
        replaceSkin(player, skin);
        VRNUtil.say(player, "&aYour skin has been changed. To reset it, run /skin.");
    }

    private void replaceSkin(Player player, Skin skin) {
        final Property property = api.getTexturesProperty(player);
        if (!skinCache.containsKey(player.getUniqueId())) {
            skinCache.put(player.getUniqueId(), property);
        }
        Property newProperty = skin != null ? new Property("textures", skin.getTexture(), skin.getSignature()) : skinCache.remove(player.getUniqueId());
        api.replaceProperty(player, "textures", property, newProperty);
        api.reloadSkin(player);
        player.updateInventory();
        final PlayerInventory inventory = player.getInventory();
        inventory.setHeldItemSlot(inventory.getHeldItemSlot());
        player.setExp(player.getExp());
        player.setTotalExperience(player.getTotalExperience());
        player.getInventory().setItemInMainHand(player.getInventory().getItemInMainHand());
        player.getInventory().setItemInOffHand(player.getInventory().getItemInOffHand());
        player.setWalkSpeed(player.getWalkSpeed());
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            pl.hidePlayer(VRNCore.getInstance(), player);
            pl.showPlayer(VRNCore.getInstance(), player);
        }
    }
}
