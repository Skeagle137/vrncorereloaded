package net.skeagle.vrncore.commands.nicknames;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Realname extends SimpleCommand {

    public Realname() {
        super("realname");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Look up a player's real name from their nick name.");
        setPermission("vrn.realname");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (ChatColor.stripColor(pl.getDisplayName()).equals(ChatColor.stripColor(args[0]))) {
                say(getSender(), "&7The player with the nickname " + pl.getDisplayName() + "&r&7 has the real name of &a" + pl.getName() + "&7.");
                return;
            }
        }
        say(getSender(), "&cThere is no player online that has that nickname.");
    }
}
