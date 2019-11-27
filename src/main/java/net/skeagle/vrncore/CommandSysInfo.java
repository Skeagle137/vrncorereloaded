package net.skeagle.vrncore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class CommandSysInfo implements CommandExecutor {
	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
	    Player p = (Player)sender;
		if (p.hasPermission("vrn.sysinfo")) {
			p.sendMessage(" - Server Name : VRN Network");
			p.sendMessage(" - Server OS Name : " + System.getProperty("os.name"));
			p.sendMessage(" - Server OS Version : " + System.getProperty("os.version"));
			p.sendMessage(" - Server OS Arch : " + System.getProperty("os.arch"));
			p.sendMessage(" - Server OS Java version : " + System.getProperty("java.version"));
		}
		else {
			p.sendMessage(VRNcore.noperm);
		}
		return true;
	}
}
