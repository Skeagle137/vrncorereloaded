package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
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
        final PlayerData data = PlayerManager.getData(target != null && target != player ? target.getUniqueId() : player.getUniqueId());
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
                (data.getPlayer() == player ? "your" : "&a" + data.getName() + "&7's") + " nick to " + nick + "&r&7.");
        data.setNick(nick);
        if (data.getPlayer() == player) return;
        say(target, "&7Your nickname was changed to " + data.getName());
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
        final PlayerData data = PlayerManager.getData(target != null && target != player ? target.getUniqueId() : player.getUniqueId());
        data.setNick(color(data.getPlayer().getName()));
        data.getPlayer().setDisplayName(color(data.getPlayer().getName()));
        data.getPlayer().setPlayerListName(color(data.getPlayer().getName()));
        say(player, data.getPlayer() == player ? "&7Removed your nickname." : "&7Removed nickname for &a" + data.getName() + ".");
        if (data.getPlayer() == player) return;
        say(target, "&7Your nickname was disabled.");
    }
}
