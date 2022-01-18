package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncommands.BukkitUtils.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class NickCommands {

    @CommandHook("nick")
    public void onNick(final CommandSender sender, final Player target, final String nick) {
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (nick.equalsIgnoreCase(sender.getName())) {
                say(sender, "&cThe nickname can't be your own name.");
                return;
            }
            if (ChatColor.stripColor(nick).equals(ChatColor.stripColor(pl.getDisplayName()))) {
                say(sender, "&cAnother player already has this nickname.");
                return;
            }
        }
        say(sender, "&7Successfully set " +
                (data.getPlayer() == sender ? "your" : "&a" + data.getName() + "&7's") + " nick to " + nick + "&r&7.");
        data.setNick(nick);
        if (data.getPlayer() == sender) return;
        say(target, "&7Your nickname was changed to " + data.getName());
    }

    @CommandHook("realname")
    public void onRealname(final CommandSender sender, final String nick) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(pl.getDisplayName()).equals(ChatColor.stripColor(nick))) {
                say(sender, "&7The player with the nickname " + PlayerManager.getData(pl.getUniqueId()).getNick() + "&r&7 has the real name of &a" + pl.getName() + "&7.");
                return;
            }
        }
        say(sender, "&cThere is no player online that has that nickname.");
    }

    @CommandHook("removenick")
    public void onRemoveNick(final CommandSender sender, final Player target) {
        final PlayerData data = PlayerManager.getData(target.getUniqueId());
        data.setNick(null);
        say(sender, data.getPlayer() == sender ? "&7Removed your nickname." : "&7Removed nickname for &a" + data.getName() + ".");
        if (data.getPlayer() == sender) return;
        say(target, "&7Your nickname was disabled.");
    }
}
