package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandClearChat implements CommandExecutor {
	@Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    Player p = (Player)sender;
        if (p.hasPermission("vrn.clearchat")) {
            for (int i = 0; i < 150; i++) {
                Bukkit.broadcastMessage("");
            }
            Bukkit.broadcastMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + p.getName() + " &7has cleared the chat."));
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
	}
}
