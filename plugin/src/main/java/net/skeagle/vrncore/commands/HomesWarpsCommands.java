package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNCore;
import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.GUIs.WarpsGUI;
import net.skeagle.vrncore.Settings;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import net.skeagle.vrnlib.misc.Task;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.concurrent.CompletableFuture;

import static net.skeagle.vrncore.utils.VRNUtil.say;

@SuppressWarnings("unused")
public class HomesWarpsCommands {

    private final VRNCore plugin;

    public HomesWarpsCommands(VRNCore plugin) {
        this.plugin = plugin;
    }

    @CommandHook("home")
    public void onHome(Player player, Home h) {
        sayTp(player);
        player.teleport(h.location());
    }

    @CommandHook("sethome")
    public void onSetHome(Player player, String name) {
        if (!player.hasPermission("vrn.homes.limit.*")) {
            int limit = VRNUtil.getLimitForPerm(player, "vrn.homes.limit", Settings.maxHomes);
            if (limit != 0 && plugin.getHomeManager().getHomeNames(player).size() >= limit) {
                say(player, "&cYou can only set a maximum of " + limit + " homes. Delete some of your homes if you want to set more.");
                return;
            }
        }
        if (plugin.getHomeManager().getHome(name, player) != null) {
            say(player, "&cA home with that name already exists.");
            return;
        }
        plugin.getHomeManager().createHome(name, player);
        say(player, "&7Home set, teleport to it with &a/home " + name + "&7.");
    }

    @CommandHook("delhome")
    public void onDelHome(CommandSender sender, Home h) {
        say(sender, "&7Home &a" + h.name() + "&7 successfully deleted.");
        plugin.getHomeManager().deleteHome(h);
    }

    @CommandHook("homes")
    public void onHomes(Player player, CompletableFuture<OfflinePlayer> target) {
        target.thenAcceptAsync(offPlayer -> {
            HomeManager manager = plugin.getHomeManager();
            manager.getHomesCount(offPlayer).thenAccept(count -> {
                if (count >= 1) {
                    Task.syncDelayed(() -> new HomesGUI(manager, player, offPlayer));
                    return;
                }
                say(player, "&c" + (player != offPlayer ? offPlayer.getName() + " does" : "You do") + " not have any homes available.");
            });
        });
    }

    @CommandHook("warp")
    public void onWarp(Player player, Warp w) {
        sayTp(player);
        player.teleport(w.location());
    }

    @CommandHook("setwarp")
    public void onSetWarp(Player player, String name) {
        if (!player.hasPermission("vrn.warps.limit.*")) {
            int limit = VRNUtil.getLimitForPerm(player, "vrn.warps.limit", Settings.maxWarps);
            if (limit != 0 && plugin.getWarpManager().getWarpsOwned(player) >= limit) {
                say(player, "&cYou can only set a maximum of " + limit + " warps. Delete some of your warps if you want to set more.");
                return;
            }
        }
        if (plugin.getWarpManager().getWarp(name) != null) {
            say(player, "&cA warp with that name already exists.");
            return;
        }
        plugin.getWarpManager().createWarp(player, name);
        say(player, "&7Warp set, teleport to it with &a/warp " + name + "&7.");
    }

    @CommandHook("delwarp")
    public void onDelWarp(CommandSender sender, Warp w) {
        say(sender, "&7Warp &a" + w.name() + "&7 successfully deleted.");
        plugin.getWarpManager().deleteWarp(w);
    }

    @CommandHook("warps")
    public void onWarps(Player player) {
        WarpManager manager = plugin.getWarpManager();
        if (plugin.getWarpManager().getWarps().size() >= 1) {
            new WarpsGUI(manager, player);
            return;
        }
        say(player, "&cThere are no warps available.");
    }

    private void sayTp(Player player) {
        say(player, BukkitMessages.msg("teleporting"));
    }
}
