package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.GUIs.WarpsGUI;
import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.homes.Home;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.Messages;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class HomesWarpsCommands {

    @CommandHook("home")
    public void onHome(final Player player, final Home h) {
        sayTp(player);
        player.teleport(h.getLocation());
    }

    @CommandHook("sethome")
    public void onSetHome(final Player player, final String name) {
        if (!player.hasPermission("vrn.homelimit.*")) {
            for (int i = 0; i == VRNcore.getInstance().getHomeManager().getHomeNames(player).size(); i++)
                if (player.hasPermission("vrn.homelimit." + i)) {
                    say(player, "&cYou can only set a maximum of " + i + " homes. Delete some of your homes if you want to set more.");
                    return;
                }
        }
        if (VRNcore.getInstance().getHomeManager().getHome(name, player) != null) {
            say(player, "&cA home with that name already exists.");
            return;
        }
        VRNcore.getInstance().getHomeManager().createHome(name, player);
        say(player, "&7Home set, teleport to it with &a/home " + name + "&7.");
    }

    @CommandHook("delhome")
    public void onDelHome(final CommandSender sender, final Home h) {
        say(sender, "&7Home &a" + h.getName() + "&7 successfully deleted.");
        VRNcore.getInstance().getHomeManager().deleteHome(h);
    }

    @CommandHook("homes")
    public void onHomes(final Player player, final Player target) {
        final Player homesPlayer = target != null && target != player ? target : player;
        if (VRNcore.getInstance().getHomeManager().getHomeNames(player).size() >= 1) {
            new HomesGUI(homesPlayer).open(player);
            return;
        }
        say(player, "&c" + (player != homesPlayer ? homesPlayer.getName() + " does" : "You do") + " not have any homes available.");
    }

    @CommandHook("warp")
    public void onWarp(final Player player, final Warp w) {
        sayTp(player);
        player.teleport(w.getLocation());
    }

    @CommandHook("setwarp")
    public void onSetWarp(final Player player, final String name) {
        if (!player.hasPermission("vrn.warplimit.*")) {
            for (int i = 0; i == VRNcore.getInstance().getHomeManager().getHomeNames(player).size(); i++)
                if (player.hasPermission("vrn.warplimit." + i)) {
                    say(player, "&cYou can only set a maximum of " + i + " warps. Delete some of your warps if you want to set more.");
                    return;
                }
        }
        if (VRNcore.getInstance().getWarpManager().getWarp(name) != null) {
            say(player, "&cA warp with that name already exists.");
            return;
        }
        VRNcore.getInstance().getWarpManager().createWarp(player, name);
        say(player, "&7Warp set, teleport to it with &a/warp " + name + "&7.");
    }

    @CommandHook("delwarp")
    public void onDelWarp(final CommandSender sender, final Warp w) {
        say(sender, "&7Home &a" + w.getName() + "&7 successfully deleted.");
        VRNcore.getInstance().getWarpManager().deleteWarp(w);
    }

    @CommandHook("warps")
    public void onWarps(final Player player) {
        if (VRNcore.getInstance().getWarpManager().getWarps().size() >= 1) {
            new WarpsGUI().open(player);
            return;
        }
        say(player, "&cThere are no warps available.");
    }

    private void sayTp(final Player player) {
        say(player, Messages.msg("teleporting"));
    }
}
