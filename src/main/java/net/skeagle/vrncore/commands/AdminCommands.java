package net.skeagle.vrncore.commands;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.minecraft.server.level.ServerPlayer;
import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.GUIs.GivePlusGUI;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.LuckPermsHook;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.playerdata.PlayerStates;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.misc.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.OfflinePlayer;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_18_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.plugin.Plugin;

import java.util.*;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;
import static net.skeagle.vrnlib.misc.TimeUtil.parseTimeString;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class AdminCommands {

    private final Plugin plugin;
    private final List<Player> vanished;

    public AdminCommands(Plugin plugin) {
        this.plugin = plugin;
        vanished = new ArrayList<>();
        if (HookManager.isLuckPermsLoaded()) {
            new LuckPermsHook().getLuckperms().getEventBus().subscribe(this.plugin, NodeMutateEvent.class, e -> {
                if (!e.isUser()) return;
                Player p = Bukkit.getPlayer(((User) e.getTarget()).getUniqueId());
                if (p == null) return;
                if (!PlayerManager.getData(p.getUniqueId()).getStates().isVanished()) {
                    return;
                }
                String perm = "vrn.vanish";
                if (e.getDataBefore().stream().anyMatch(n -> n.getKey().startsWith(perm)) && e.getDataAfter().stream().noneMatch(n -> n.getKey().startsWith(perm))) {
                    updateVanishedPlayers(p);
                }
            });
        }

        new EventListener<>(PlayerQuitEvent.class, e ->
                vanished.remove(e.getPlayer()));

        new EventListener<>(PlayerJoinEvent.class, e -> {
            Task.syncDelayed(() -> {
                if (PlayerManager.getData(e.getPlayer().getUniqueId()).getStates().isVanished()) {
                    vanished.add(e.getPlayer());
                }
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl == e.getPlayer()) continue;
                    if (vanished.contains(e.getPlayer())) {
                        if (!VRNUtil.hasVanishPriority(e.getPlayer(), pl)) continue;
                        addVanish(e.getPlayer(), pl);
                    }
                    if (vanished.contains(pl)) {
                        if (!VRNUtil.hasVanishPriority(pl, e.getPlayer())) continue;
                        addVanish(pl, e.getPlayer());
                    }
                }
            });
        });

        new EventListener<>(EntityTargetLivingEntityEvent.class, EventPriority.HIGH, e -> {
            if (!(e.getTarget() instanceof Player player)) return;
            if (PlayerManager.getData(player.getUniqueId()).getStates().isVanished()) {
                e.setCancelled(true);
            }
        });

        new EventListener<>(TabCompleteEvent.class, e -> {
            if (!(e.getSender() instanceof Player player)) return;
            final List<String> list = e.getCompletions();
            for (Player pl : vanished) {
                if (pl == player) continue;
                if (VRNUtil.hasVanishPriority(pl, player)) {
                    list.remove(pl.getName());
                }
            }
            e.setCompletions(list);
        });

        new EventListener<>(PlayerCommandPreprocessEvent.class, e -> {
            if (e.isCancelled()) return;
            for (Player pl : vanished) {
                if (pl == e.getPlayer()) continue;
                if (VRNUtil.hasVanishPriority(pl, e.getPlayer())) {
                    e.setMessage(e.getMessage().replaceAll(pl.getName(), pl.getName() + "§§"));
                }
            }
        });
    }

    @CommandHook("broadcast")
    public void onBroadcast(final CommandSender sender, final String message) {
        Bukkit.broadcastMessage(color(BukkitMessages.msg("broadcastPrefix") + " " + message));
    }

    @CommandHook("echest")
    public void onEchest(final Player player, final Player target) {
        player.openInventory(target.getEnderChest());
        say(player, target != player ? "Now showing &a" + target.getName() + "&7's ender chest." : "Now showing your ender chest.");
    }

    @CommandHook("vanish")
    public void onVanish(final CommandSender sender, final OfflinePlayer target, boolean silent) {
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        final PlayerStates states = data.getStates();
        if (target.getPlayer() != null) {
            Player player = target.getPlayer();
            if (!states.isVanished()) {
                this.vanished.add(player);
            }
            else {
                this.vanished.remove(player);
            }
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (pl == player || VRNUtil.hasVanishPriority(pl, player)) continue;
                if (!states.isVanished()) {
                    addVanish(player, pl);
                } else {
                    removeVanish(player, pl);
                }
                if (!silent) {
                    sayNoPrefix(pl, Settings.joinLeaveEnabled ? BukkitMessages.msg(!states.isVanished() ? "leaveMsg" : "joinMsg").replaceAll("%player%", data.getName()) :
                            "&e" + target.getName() + (!states.isVanished() ? " left" : " joined") + " the game.");
                }
            }
        }
        states.setVanished(!states.isVanished());
        say(target.getPlayer(), "Vanish " + (states.isVanished() ? "enabled." : "disabled."));
        if (target == sender) return;
        say(sender, "Vanish " + (states.isVanished() ? "enabled" : "disabled") + " for &a" + target.getName() + "&7.");
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
        target.setFireTicks(0);
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
    public void onTimePlayedGet(final CommandSender sender, final OfflinePlayer target) {
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        say(sender, (target == sender ? "Your" : "&a" + target.getName() + "&7's") +
                " time played is &a" + timeToMessage(data.getTimePlayed()) + "&7.");
    }

    @CommandHook("timeplayedset")
    public void onTimePlayedSet(final CommandSender sender, final OfflinePlayer target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        data.setTimePlayed(totalsec);
        say(sender, "Time played set to &a" + timeToMessage(totalsec) + (target == sender ? "&7." : "&7 for &a" + target.getName() + "&7."));
    }

    @CommandHook("timeplayedadd")
    public void onTimePlayedAdd(final CommandSender sender, final OfflinePlayer target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        final long total = data.getTimePlayed() + totalsec;
        data.setTimePlayed(total);
        say(sender, "Added &a" + timeToMessage(totalsec) + "&7 to " + (target == sender ? "your" : "&a" + target.getName() + "&7's") +
                " time. " + (target == sender ? "Your" : "Their") + " total time is now &a" + timeToMessage(total) + "&7.");
    }

    @CommandHook("timeplayedsubtract")
    public void onTimePlayedSubtract(final CommandSender sender, final OfflinePlayer target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(sender, e.getMessage());
            return;
        }
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        final long total = data.getTimePlayed() - totalsec;
        if (total < 0) {
            data.setTimePlayed(0L);
            say(sender, "Time played set to &a0 seconds &7" + (target == sender ? "." : " for &a" + target.getName() + "&7."));
            return;
        }
        data.setTimePlayed(total);
        say(sender, "Subtracted &a" + timeToMessage(totalsec) + "&7 from " + (target == sender ? "your" : "&a" + target.getName() + "&7's") +
                " time. " + (target == sender ? "Your" : "Their") + " total time is now &a" + timeToMessage(total) + "&7.");
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
    public void onGod(final CommandSender sender, final OfflinePlayer target) {
        final PlayerStates data = PlayerManager.getData(target.getUniqueId()).getStates();
        data.setGodmode(!data.hasGodmode());
        if (target.getPlayer() != null) {
            say(target.getPlayer(), "You are " + (data.hasGodmode() ? "now" : "no longer") + " invulnerable.");
        }
        if (target == sender) return;
        say(sender, "&a" + target.getName() + " &7is " + (data.hasGodmode() ? "now" : "no longer") + " invulnerable.");
    }

    private void addVanish(Player vanished, Player viewer) {
        ServerPlayer nmsPlayer = ((CraftPlayer) viewer).getHandle();
        nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, ((CraftPlayer) vanished).getHandle()));
        viewer.hidePlayer(plugin, vanished);
    }

    private void removeVanish(Player vanished, Player viewer) {
        ServerPlayer nmsPlayer = ((CraftPlayer) viewer).getHandle();
        nmsPlayer.connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, ((CraftPlayer) vanished).getHandle()));
        viewer.showPlayer(plugin, vanished);
    }

    private void updateVanishedPlayers(Player player) {
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!VRNUtil.hasVanishPriority(pl, player)) {
                removeVanish(pl, player);
                continue;
            }
            addVanish(pl, player);
        }
    }
}
