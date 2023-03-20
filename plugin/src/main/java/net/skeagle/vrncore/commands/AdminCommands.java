package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.BukkitUtils;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.GUIs.GivePlusGUI;
import net.skeagle.vrncore.playerdata.PlayerStates;
import net.skeagle.vrnlib.misc.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import static net.skeagle.vrncore.utils.VRNUtil.*;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class AdminCommands {

    @CommandHook("broadcast")
    public void onBroadcast(final CommandSender sender, final String message) {
        Bukkit.broadcastMessage(BukkitUtils.color(BukkitMessages.msg("broadcastPrefix") + " " + message));
    }

    @CommandHook("echest")
    public void onEchest(final Player player, final Player target) {
        player.openInventory(target.getEnderChest());
        say(player, target != player ? "Now showing &a" + target.getName() + "&7's ender chest." : "Now showing your ender chest.");
    }

    @CommandHook("giveplus")
    public void onGivePlus(final Player player) {
        new GivePlusGUI(player);
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
    public void onHeal(final CommandSender sender, final Player target) {
        target.setHealth(target.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
        target.setFoodLevel(20);
        target.setSaturation(5);
        target.setFireTicks(0);
        target.getActivePotionEffects().clear();
        say(target, "Your health and hunger are now full.");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + "&7's health and hunger are now full.");
    }

    @CommandHook("speed")
    public void onSpeed(final CommandSender sender, final Player target, final int speed) {
        if (speed == 1) {
            if (target.isFlying())
                target.setFlySpeed((float) 0.1);
            else
                target.setWalkSpeed((float) 0.2);
            say(target, "Your " + (target.isFlying() ? "flying" : "walking") + " speed has been reset.");
            if (target == sender) return;
            say(sender, "&a" + target.getName() + "&7's " + (target.isFlying() ? "flying" : "walking") + " speed has been reset.");
        } else {
            float f = speed;
            if (target.isFlying()) {
                if (speed > 10)
                    f = 10;
                if (speed < 0)
                    f = 0;
                target.setFlySpeed(f / 10);
            } else {
                if (speed > 10)
                    f = 10;
                if (speed < 0)
                    f = 0;
                target.setWalkSpeed(0.12f + (0.088f * f));
            }
            say(target, "Your " + (target.isFlying() ? "flying" : "walking") + " speed has been set to &a" +
                    (target.isFlying() ? f : (int) ((0.12f + (0.088f * f)) * 10)) + "&7.");
            if (target == sender) return;
            say(sender, "&a" + target.getName() + "&7's " + (target.isFlying() ? "flying" : "walking") + " speed has been set to &a" +
                    (target.isFlying() ? f : (int) ((0.12f + (0.088f * f)) * 10)) + "&7.");
        }
    }

    @CommandHook("timeplayedget")
    public void onTimePlayedGet(final CommandSender sender, final CompletableFuture<OfflinePlayer> target) {
        target.thenComposeAsync(offPlayer -> VRNCore.getPlayerData(offPlayer.getUniqueId()).thenAcceptAsync(data -> {
            VRNCore.getPlayerData(offPlayer.getUniqueId());
            say(sender, (offPlayer == sender ? "Your" : "&a" + offPlayer.getName() + "&7's") +
                    " time played is &a" + timeToMessage(data.getTimePlayed()) + "&7.");
        }));
    }

    @CommandHook("timeplayedset")
    public void onTimePlayedSet(final CommandSender sender, final CompletableFuture<OfflinePlayer> target, final String time) {
        final long totalsec;
        try {
            totalsec = TimeUtil.parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        target.thenComposeAsync(offPlayer -> VRNCore.getPlayerData(offPlayer.getUniqueId()).thenAcceptAsync(data -> {
            data.setTimePlayed(totalsec);
            say(sender, "Time played set to &a" + TimeUtil.timeToMessage(totalsec) + (offPlayer == sender ? "&7." : "&7 for &a" + offPlayer.getName() + "&7."));
        }));
    }

    @CommandHook("timeplayedadd")
    public void onTimePlayedAdd(final CommandSender sender, final CompletableFuture<OfflinePlayer> target, final String time) {
        final long totalsec;
        try {
            totalsec = TimeUtil.parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        target.thenComposeAsync(offPlayer -> VRNCore.getPlayerData(offPlayer.getUniqueId()).thenAcceptAsync(data -> {
            final long total = data.getTimePlayed() + totalsec;
            data.setTimePlayed(total);
            say(sender, "Added &a" + TimeUtil.timeToMessage(totalsec) + "&7 to " + (offPlayer == sender ? "your" : "&a" + offPlayer.getName() + "&7's") +
                    " time. " + (offPlayer == sender ? "Your" : "Their") + " total time is now &a" + TimeUtil.timeToMessage(total) + "&7.");
        }));
    }

    @CommandHook("timeplayedsubtract")
    public void onTimePlayedSubtract(final CommandSender sender, final CompletableFuture<OfflinePlayer> target, final String time) {
        final long totalsec;
        try {
            totalsec = TimeUtil.parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        target.thenComposeAsync(offPlayer -> VRNCore.getPlayerData(offPlayer.getUniqueId()).thenAcceptAsync(data -> {
            final long total = data.getTimePlayed() - totalsec;
            if (total < 0) {
                data.setTimePlayed(0L);
                say(sender, "Time played set to &a0 seconds &7" + (offPlayer == sender ? "." : " for &a" + offPlayer.getName() + "&7."));
                return;
            }
            data.setTimePlayed(total);
            say(sender, "Subtracted &a" + TimeUtil.timeToMessage(totalsec) + "&7 from " + (offPlayer == sender ? "your" : "&a" + offPlayer.getName() + "&7's") +
                    " time. " + (offPlayer == sender ? "Your" : "Their") + " total time is now &a" + TimeUtil.timeToMessage(total) + "&7.");
        }));
    }

    @CommandHook("fly")
    public void onFly(final CommandSender sender, final Player target) {
        target.setAllowFlight(!target.getAllowFlight());
        say(target, "Fly mode has been " + (target.getAllowFlight() ? "enabled" : "disabled") + ".");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + "&7's fly mode has been " + (target.getAllowFlight() ? "enabled" : "disabled") + ".");
    }

    @CommandHook("gms")
    public void onGmSurvival(final CommandSender sender, final Player target) {
        target.setGameMode(GameMode.SURVIVAL);
        say(target, "You are now in &asurvival&7 mode.");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + " &7is now in &asurvival&7 mode.");
    }

    @CommandHook("gmc")
    public void onGmCreative(final CommandSender sender, final Player target) {
        target.setGameMode(GameMode.CREATIVE);
        say(target, "You are now in &acreative&7 mode.");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + " &7is now in &acreative&7 mode.");
    }

    @CommandHook("gma")
    public void onGmAdventure(final CommandSender sender, final Player target) {
        target.setGameMode(GameMode.ADVENTURE);
        say(target, "You are now in &aadventure&7 mode.");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + " &7is now in &aadventure&7 mode.");
    }

    @CommandHook("gmsp")
    public void onGmSpectator(final CommandSender sender, final Player target) {
        target.setGameMode(GameMode.SPECTATOR);
        say(target, "You are now in &aspectator&7 mode.");
        if (target == sender) return;
        say(sender, "&a" + target.getName() + " &7is now in &aspectator&7 mode.");
    }

    @CommandHook("god")
    public void onGod(final CommandSender sender, final CompletableFuture<OfflinePlayer> target) {
        target.thenComposeAsync(offPlayer -> VRNCore.getPlayerData(offPlayer.getUniqueId()).thenAcceptAsync(data -> {
            PlayerStates states = data.getStates();
            states.setGodmode(!states.hasGodmode());
            if (offPlayer.getPlayer() != null) {
                say(offPlayer.getPlayer(), "You are " + (states.hasGodmode() ? "now" : "no longer") + " invulnerable.");
            }
            if (offPlayer == sender) return;
            say(sender, "&a" + offPlayer.getName() + " &7is " + (states.hasGodmode() ? "now" : "no longer") + " invulnerable.");
        }));
    }
}
