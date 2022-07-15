package net.skeagle.vrncore.commands;

import net.skeagle.vrncommands.CommandHook;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class NickCommands {

    private final VRNcore plugin;

    public NickCommands(VRNcore plugin) {
        this.plugin = plugin;
    }

    @CommandHook("nick") @SuppressWarnings("deprecated")
    public void onNick(final CommandSender sender, final Player target, final String nick) {
        String stripped = ChatColor.stripColor(nick);
        if (stripped.equalsIgnoreCase(sender.getName())) {
            say(sender, "&cThe nickname can't be your own name.");
            return;
        }
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (stripped.equals(ChatColor.stripColor(pl.getDisplayName()))) {
                say(sender, "&cAnother player already has this nickname.");
                return;
            }
        }
        plugin.getPlayerManager().getData(target.getUniqueId()).thenAccept(data -> {
            data.setNick(nick);
            say(sender, "&7Set " + (data.getPlayer() == sender ? "your" : "&a" + target.getName() + "&7's") + " nick to " + nick + "&r&7.");
            if (sender == target) return;
            say(target, "&7Your nickname was changed to " + data.getName());
        });
    }

    @CommandHook("realname")
    public void onRealname(final CommandSender sender, final String nick) {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            String stripped = ChatColor.stripColor(pl.getDisplayName());
            if (stripped.equals(ChatColor.stripColor(nick))) {
                if (pl.getName().equals(stripped)) {
                    say(sender, "&c" + pl.getName() + " does not have a nickname.");
                    return;
                }
                plugin.getPlayerManager().getData(pl.getUniqueId()).thenAcceptAsync(data ->
                        say(sender, "&7The player with the nickname " + pl.getDisplayName() + "&r&7 has the real name of &a" + pl.getName() + "&7."));
                return;
            }
        }
        say(sender, "&cThere is no player online that has that nickname.");
    }

    @CommandHook("removenick")
    public void onRemoveNick(final CommandSender sender, final Player target) {
        plugin.getPlayerManager().getData(target.getUniqueId()).thenAcceptAsync(data -> {
            data.setNick(null);
            say(sender, data.getPlayer() == sender ? "&7Removed your nickname." : "&7Removed nickname for &a" + data.getName() + ".");
            if (sender == target) return;
            say(target, "&7Your nickname was disabled.");
        });
    }
}
