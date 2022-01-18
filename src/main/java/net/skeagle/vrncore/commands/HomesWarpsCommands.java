package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.BukkitMessages;
import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.GUIs.WarpsGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesWarpsCommands {

    private VRNcore plugin;

    public HomesWarpsCommands(VRNcore plugin) {
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
            for (int i = 0; i <= plugin.getHomeManager().getHomeNames(player).size(); i++) {
                if (player.hasPermission("vrn.homelimit." + i)) {
                    say(player, "&cYou can only set a maximum of " + i + " homes. Delete some of your homes if you want to set more.");
                    return;
                }
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
    public void onHomes(Player player, Player target) {
        HomeManager manager = plugin.getHomeManager();
        if (manager.getHomeNames(target).size() >= 1) {
            new HomesGUI(manager, player, target);
            return;
        }
        say(player, "&c" + (player != target ? target.getName() + " does" : "You do") + " not have any homes available.");
    }

    @CommandHook("warp")
    public void onWarp(Player player, Warp w) {
        sayTp(player);
        player.teleport(w.location());
    }

    @CommandHook("setwarp")
    public void onSetWarp(Player player, String name) {
        if (!player.hasPermission("vrn.warplimit.*")) {
            for (long i = 0; i <= plugin.getWarpManager().getWarpsOwned(player); i++) {
                if (player.hasPermission("vrn.warplimit." + i)) {
                    say(player, "&cYou can only set a maximum of " + i + " warps. Delete some of your warps if you want to set more.");
                    return;
                }
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
