package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNPlayer;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class NickCommands {

    @CommandHook("nick")
    public void onNick(final Player player, final Player target, final String nick) {
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (nick.equalsIgnoreCase(player.getName())) {
                say(player, "&cThe nickname can't be your own name.");
                return;
            }
            if (ChatColor.stripColor(nick).equals(ChatColor.stripColor(pl.getDisplayName()))) {
                say(player, "&cAnother player already has this nickname.");
                return;
            }
        }
        say(player, "&7Successfully set " +
                (vrnPlayer.getPlayer() == player ? "your" : "&a" + vrnPlayer.getName() + "&7's") + " nick to " + nick + "&r&7.");
        vrnPlayer.setName(nick);
        if (vrnPlayer.getPlayer() == player) return;
        say(vrnPlayer, "&7Your nickname was changed to " + vrnPlayer.getName());
    }

    @CommandHook("realname")
    public void onRealname(final CommandSender sender, final String nick) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(pl.getDisplayName()).equals(ChatColor.stripColor(nick))) {
                say(sender, "&7The player with the nickname " + pl.getDisplayName() + "&r&7 has the real name of &a" + pl.getName() + "&7.");
                return;
            }
        }
        say(sender, "&cThere is no player online that has that nickname.");
    }

    @CommandHook("removenick")
    public void onRemoveNick(final Player player, final Player target) {
        final VRNPlayer vrnPlayer = new VRNPlayer(target != null && target != player ? target : player);
        vrnPlayer.setName(color(vrnPlayer.getPlayer().getName()));
        vrnPlayer.getPlayer().setDisplayName(color(vrnPlayer.getPlayer().getName()));
        vrnPlayer.getPlayer().setPlayerListName(color(vrnPlayer.getPlayer().getName()));
        say(player, vrnPlayer.getPlayer() == player ? "&7Removed your nickname." : "&7Removed nickname for &a" + vrnPlayer.getName() + ".");
        if (vrnPlayer.getPlayer() == player) return;
        say(vrnPlayer, "&7Your nickname was disabled.");
    }
}
