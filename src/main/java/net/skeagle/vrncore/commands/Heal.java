package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Heal extends SimpleCommand {

    public Heal() {
        super("heal");
    }

    @Override
    protected void onCommand() {
		if (args.length == 0) {
		    checkConsole();
            Player p = getPlayer();
            if (p.hasPermission("vrn.heal.self")) {
                p.setHealth(20);
                p.setFoodLevel(20);
                p.setFireTicks(0);
                p.sendMessage(VRNcore.vrn + "Your health and hunger are now full.");
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        else if (args.length == 1) {
            checkConsole();
            Player p = getPlayer();
            if (p.hasPermission("vrn.heal.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    a.setHealth(20);
                    a.setFoodLevel(20);
                    a.setFireTicks(0);
                    a.sendMessage(VRNcore.vrn + "Your health and hunger are now full.");
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + "&7's health and hunger are now full."));
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
	}
}
