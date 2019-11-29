package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Clearchat extends SimpleCommand {

    public Clearchat() {
        super("clearchat");
    }

    @Override
    protected void onCommand() {
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
	}
}
