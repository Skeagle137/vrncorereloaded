package net.skeagle.vrncore.commands;

import net.minecraft.network.protocol.game.ClientboundPlayerInfoPacket;
import net.skeagle.vrncore.GUIs.GivePlusGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNPlayer;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.EventListener;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.misc.TimeUtil;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_17_R1.entity.CraftPlayer;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Mob;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.EntityTargetEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.TabCompleteEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrnlib.misc.TimeUtil.parseTimeString;
import static net.skeagle.vrnlib.misc.TimeUtil.timeToMessage;

public class AdminCommands {

    private final List<UUID> vanished;

    public AdminCommands() {
        vanished = new ArrayList<>();
        new EventListener<>(PlayerQuitEvent.class, e ->
                vanished.remove(e.getPlayer().getUniqueId()));

        new EventListener<>(PlayerJoinEvent.class, e -> {
            final VRNPlayer vrnPlayer = new VRNPlayer(e.getPlayer());
            if (vrnPlayer.isVanished()) {
                for (final Player pl : Bukkit.getOnlinePlayers()) {
                    if (pl == e.getPlayer()) continue;
                    vanished.add(vrnPlayer.getPlayer().getUniqueId());
                    Task.syncDelayed(() -> {
                        ((CraftPlayer) pl).getHandle().connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER,
                                ((CraftPlayer) e.getPlayer()).getHandle()));
                        pl.hidePlayer(VRNcore.getInstance(), e.getPlayer());
                    }, 3);
                }
            }
        });

        new EventListener<>(EntityTargetEvent.class, e -> {
            if (!(e.getTarget() instanceof Player)) return;
            final VRNPlayer p = new VRNPlayer((Player) e.getTarget());
            if (p.isVanished()) {
                e.setCancelled(true);
                e.setTarget(null);
            }
            if (e.getEntity() instanceof Mob)
                ((Mob) e.getEntity()).setTarget(null);
        });

        new EventListener<>(TabCompleteEvent.class, e -> {
            if (!(e instanceof Player player)) return;
            final List<String> list = e.getCompletions();
            for (final UUID uuid : vanished) {
                if (list.contains(uuid.toString())) {
                    if (uuid.equals(player.getUniqueId())) continue;
                    final Player p = Bukkit.getPlayer(uuid);
                    if (p == null) continue;
                    list.remove(p.getName());
                }
            }
            e.setCompletions(list);
        });
    }

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
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null ? target : player);
        if (!vrnPlayer.isVanished()) {
            this.vanished.add(vrnPlayer.getPlayer().getUniqueId());
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (pl == vrnPlayer.getPlayer()) continue;
                pl.hidePlayer(VRNcore.getInstance(), vrnPlayer.getPlayer());
                ((CraftPlayer) pl).getHandle().connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.REMOVE_PLAYER, ((CraftPlayer) vrnPlayer.getPlayer()).getHandle()));
            }
        } else {
            this.vanished.remove(vrnPlayer.getPlayer().getUniqueId());
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (pl == vrnPlayer.getPlayer()) continue;
                pl.showPlayer(VRNcore.getInstance(), vrnPlayer.getPlayer());
                ((CraftPlayer) pl).getHandle().connection.send(new ClientboundPlayerInfoPacket(ClientboundPlayerInfoPacket.Action.ADD_PLAYER, ((CraftPlayer) vrnPlayer.getPlayer()).getHandle()));
            }
        }
        vrnPlayer.setVanished(!vrnPlayer.isVanished());
        say(vrnPlayer, "Vanish " + (vrnPlayer.isVanished() ? "enabled." : "disabled."));
        if (vrnPlayer.getPlayer() == player) return;
        say(player, "Vanish " + (vrnPlayer.isVanished() ? "enabled" : "disabled") + " for &a" + vrnPlayer.getName() + "&7.");
    }

    @CommandHook("giveplus")
    public void onGivePlus(final Player player) {
        new GivePlusGUI(player);
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
        final VRNPlayer player = new VRNPlayer(target);
        player.setMuted(!player.isMuted());
        say(player, "You are " + (player.isMuted() ? "now" : "no longer") + " muted.");
        say(sender, "&a" + player.getName() + " &7is " + (player.isMuted() ? "now" : "no longer") + " muted.");
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

    @CommandHook("invsee")
    public void onInvsee(final Player player, final Player target) {
        player.openInventory(target.getInventory());
        say(player, "Now showing &a" + target.getName() + "&7's inventory.");
        /*
        Common.runAsync(() -> {
            OfflinePlayer p = Bukkit.getOfflinePlayer(args[0]);
            File f;
            if (p.hasPlayedBefore())
                f = new File("world" + File.separator + "playerdata", p.getUniqueId().toString() + ".dat");
            else {
                say(getPlayer(), "&cno player data found for " + args[0] + ".");
                return;
            }
            try {
                NBTTagCompound nbt = NBTCompressedStreamTools.a(new FileInputStream(f));
                NBTTagList list = (NBTTagList) nbt.get("Inventory");
                if (list == null) {
                    say(getPlayer(), "&cCould not read " + p.getName() + "'s inventory.");
                    return;
                }
                List<NBTTagCompound> compoundlist = new ArrayList<>();
                for (int i = 0; i < list.size() - 1; i++) {
                    NBTTagCompound compound = (NBTTagCompound) list.get(i);
                    if (!compound.isEmpty())
                        compoundlist.add(compound);
                }
                Common.run(() -> new InvseeGUI(compoundlist, p));

            } catch (IOException e) {
                e.printStackTrace();
            }
        });

         */
    }

    @CommandHook("speed")
    public void onSpeed(final Player player, final Player target, final int speed) {
        final Player speedPlayer = target != null && target != player ? target : player;
        if (speed == 1) {
            if (speedPlayer.isFlying())
                speedPlayer.setFlySpeed((float) 0.1);
            else
                speedPlayer.setWalkSpeed((float) 0.2);
            say(speedPlayer, "Your " + (speedPlayer.isFlying() ? "flying" : "walking") + " speed has been reset.");
            if (speedPlayer == player) return;
            say(player, "&a" + speedPlayer.getName() + "&7's " + (speedPlayer.isFlying() ? "flying" : "walking") + " speed has been reset.");
        } else {
            float f = speed;
            if (speedPlayer.isFlying()) {
                if (speed > 10)
                    f = 10;
                if (speed < 0)
                    f = 0;
                speedPlayer.setFlySpeed(f / 10);
            } else {
                if (speed > 10)
                    f = 10;
                if (speed < 0)
                    f = 0;
                speedPlayer.setWalkSpeed(0.12f + (0.088f * f));
            }
            say(speedPlayer, "Your " + (speedPlayer.isFlying() ? "flying" : "walking") + " speed has been set to &a" +
                    (speedPlayer.isFlying() ? f / 10 : (int) ((0.12f + (0.088f * f)) * 10)) + "&7.");
            if (speedPlayer == player) return;
            say(player, "&a" + speedPlayer.getName() + "&7's " + (speedPlayer.isFlying() ? "flying" : "walking") + " speed has been set to &a" +
                    (speedPlayer.isFlying() ? f / 10 : (int) ((0.12f + (0.088f * f)) * 10)) + "&7.");
        }
    }

    @CommandHook("timeplayedget")
    public void onTimePlayedGet(final Player player, final Player target) {
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        say(player, (vrnPlayer.getPlayer() == player ? "Your" : "&a" + vrnPlayer.getName() + "&7's") +
                " time played is &a" + timeToMessage(vrnPlayer.getTimePlayed()) + "&7.");
    }

    @CommandHook("timeplayedset")
    public void onTimePlayedSet(final Player player, final Player target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(player, e.getMessage());
            return;
        }
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        vrnPlayer.setTimePlayed(totalsec);
        say(player, "Time played set to &a" + timeToMessage(totalsec) + (vrnPlayer.getPlayer() == player ? "&7." : "&7 for &a" + vrnPlayer.getName() + "&7."));
    }

    @CommandHook("timeplayedadd")
    public void onTimePlayedAdd(final Player player, final Player target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(player, e.getMessage());
            return;
        }
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        final long total = vrnPlayer.getTimePlayed() + totalsec;
        vrnPlayer.setTimePlayed(total);
        say(player, "Added &a" + timeToMessage(totalsec) + "&7 to " + (vrnPlayer.getPlayer() == player ? "your" : "&a" + vrnPlayer.getName() + "&7's") +
                " time. " + (vrnPlayer.getPlayer() == player ? "Your" : "Their") + " total time is now &a" + timeToMessage(total) + "&7.");
    }

    @CommandHook("timeplayedsubtract")
    public void onTimePlayedSubtract(final Player player, final Player target, final String time) {
        final long totalsec;
        try {
            totalsec = parseTimeString(time);
        } catch (final TimeUtil.TimeFormatException e) {
            say(player, e.getMessage());
            return;
        }
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        final long total = vrnPlayer.getTimePlayed() - totalsec;
        if (total < 0) {
            vrnPlayer.setTimePlayed(0);
            say(player, "Time played set to &a0 seconds &7" + (vrnPlayer.getPlayer() == player ? "." : " for &a" + vrnPlayer.getName() + "&7."));
            return;
        }
        vrnPlayer.setTimePlayed(total);
        say(player, "Subtracted &a" + timeToMessage(totalsec) + "&7 from " + (vrnPlayer.getPlayer() == player ? "your" : "&a" + vrnPlayer.getName() + "&7's") +
                " time. " + (vrnPlayer.getPlayer() == player ? "Your" : "Their") + " total time is now &a" + timeToMessage(total) + "&7.");
    }

    @CommandHook("fly")
    public void onFly(final Player player, final Player target) {
        final Player flyPlayer = target != null && target != player ? target : player;
        flyPlayer.setAllowFlight(!flyPlayer.getAllowFlight());
        say(flyPlayer, "Fly mode has been " + (flyPlayer.getAllowFlight() ? "enabled" : "disabled") + ".");
        if (flyPlayer == player) return;
        say(player, "&a" + flyPlayer.getName() + "&7's fly mode has been " + (flyPlayer.getAllowFlight() ? "enabled" : "disabled") + ".");
    }

    @CommandHook("gms")
    public void onGmSurvival(final Player player, final Player target) {
        final Player gmPlayer = target != null && target != player ? target : player;
        gmPlayer.setGameMode(GameMode.SURVIVAL);
        say(gmPlayer, "You are now in &asurvival&7 mode.");
        if (gmPlayer == player) return;
        say(player, "&a" + gmPlayer.getName() + " &7is now in &asurvival&7 mode.");
    }

    @CommandHook("gmc")
    public void onGmCreative(final Player player, final Player target) {
        final Player gmPlayer = target != null && target != player ? target : player;
        gmPlayer.setGameMode(GameMode.CREATIVE);
        say(gmPlayer, "You are now in &acreative&7 mode.");
        if (gmPlayer == player) return;
        say(player, "&a" + gmPlayer.getName() + " &7is now in &acreative&7 mode.");
    }

    @CommandHook("gma")
    public void onGmAdventure(final Player player, final Player target) {
        final Player gmPlayer = target != null && target != player ? target : player;
        gmPlayer.setGameMode(GameMode.ADVENTURE);
        say(gmPlayer, "You are now in &aadventure&7 mode.");
        if (gmPlayer == player) return;
        say(player, "&a" + gmPlayer.getName() + " &7is now in &aadventure&7 mode.");
    }

    @CommandHook("gmsp")
    public void onGmSpectator(final Player player, final Player target) {
        final Player gmPlayer = target != null && target != player ? target : player;
        gmPlayer.setGameMode(GameMode.SPECTATOR);
        say(gmPlayer, "You are now in &aspectator&7 mode.");
        if (gmPlayer == player) return;
        say(player, "&a" + gmPlayer.getName() + " &7is now in &aspectator&7 mode.");
    }

    @CommandHook("god")
    public void onGod(final Player player, final Player target) {
        final VRNPlayer godPlayer = new VRNPlayer(target != null && target != player ? target : player);
        godPlayer.setGodmode(!godPlayer.isGodmode());
        say(godPlayer, "You are " + (godPlayer.isGodmode() ? "now" : "no longer") + " invulnerable.");
        if (godPlayer.getPlayer() == player) return;
        say(player, "&a" + godPlayer.getName() + " &7is " + (godPlayer.isGodmode() ? "now" : "no longer") + " invulnerable.");
    }
}
