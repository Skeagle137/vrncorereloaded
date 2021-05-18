package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.GivePlusGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import org.bukkit.Bukkit;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.api.util.VRNUtil.color;
import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class AdminCommands {

    @CommandHook("broadcast")
    public void onBroadcast(final CommandSender sender, final String message) {
        Bukkit.broadcastMessage(color(Messages.msg("broadcastPrefix") + " " + message));
    }

    @CommandHook("clearchat")
    public void onClearChat(final CommandSender sender) {
        Bukkit.getOnlinePlayers().forEach((pl) -> {
            for (int i = 0; i < 150; i++)
                pl.sendMessage("");
        });
        Bukkit.broadcastMessage(color("&a" + sender.getName() + " &7has cleared the chat."));
    }

    @CommandHook("echest")
    public void onEchest(final Player player, final Player target) {
        player.openInventory(target != null ? target.getEnderChest() : player.getEnderChest());
        say(player, target != null && target != player ? "Now showing &a" + target.getName() + "&7's ender chest." : "Now showing your ender chest.");
    }

    @CommandHook("vanish")
    public void onVanish(final Player player, final Player target) {
        final VRNPlayer vrn = new VRNPlayer(target != null ? target : player);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!vrn.isVanished())
                pl.hidePlayer(VRNcore.getInstance(), vrn.getPlayer());
            else
                pl.showPlayer(VRNcore.getInstance(), vrn.getPlayer());
        }
        vrn.setVanished(!vrn.isVanished());
        say(vrn, "Vanish " + (vrn.isVanished() ? "enabled." : "disabled."));
        if (vrn.getPlayer() == player) return;
        say(player, "Vanish " + (vrn.isVanished() ? "enabled" : "disabled") + " for &a" + vrn.getName() + "&7.");
    }

    @CommandHook("giveplus")
    public void onGivePlus(final Player player) {
        new GivePlusGUI().displayTo(player);
    }

    @CommandHook("ban")
    public void onBan(final CommandSender sender, final Player target, final String reason, final boolean silent) {

    }

    @CommandHook("kick")
    public void onKick(final CommandSender sender, final boolean silent, final Player target, final String reason) {
        target.kickPlayer(color("&cYou have been kicked by &b" + sender.getName() + "&c" +
                (reason == null ? "." : " for: \n&6" + reason)));
        if (!silent)
            Bukkit.broadcastMessage(color("&a" + target.getName() + " &7was kicked by&c " + sender.getName() + "&7" +
                    (reason == null ? "." : " for: &b" + reason)));
    }

    @CommandHook("mute")
    public void onMute(final CommandSender sender, final Player target) {

    }

    @CommandHook("smite")
    public void onSmite(final CommandSender sender, final boolean all, final Player target) {
        if (all) {
            for (final Player pl : Bukkit.getOnlinePlayers())
                pl.getWorld().strikeLightning(pl.getLocation());
            say(sender, "Smited all players.");
            return;
        }
        target.getWorld().strikeLightning(target.getLocation());
        say(sender, "Smited &a" + target.getName() + "&7.");
    }

    @CommandHook("spawnmob")
    public void onSpawnmob(final Player player, final EntityType type, final int count) {
        if (!type.isSpawnable() && !type.isAlive()) {
            say(player, "&cThat entity type cannot be spawned.");
        }
        if (count < 1) {
            say(player, "&cMob count cannot be less than one.");
            return;
        } else if (count > 100) {
            say(player, "&cMob count cannot be over 100.");
            return;
        }
        final Block b = player.getTargetBlock(null, 50);

        for (int i = 0; i < count; i++)
            player.getWorld().spawnEntity(b.getLocation().clone().add(0.5, 1.0, 0.5), type);
        say(player, "Spawned " + count + " " + type.toString().toLowerCase() + " at &a" +
                b.getLocation().getX() + "&7, &a" + b.getLocation().getY() + "&7, &a" + b.getLocation().getZ() + "&7.");
    }

    @CommandHook("heal")
    public void onHeal(final Player player, final Player target) {
        final Player healPlayer = target != null && target != player ? target : player;
        healPlayer.setHealth(healPlayer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        healPlayer.setFoodLevel(20);
        healPlayer.setFireTicks(0);
        say(healPlayer, "Your health and hunger are now full.");
        if (healPlayer == player) return;
        say(player, "&a" + healPlayer.getName() + "&7's health and hunger are now full.");
    }
}
