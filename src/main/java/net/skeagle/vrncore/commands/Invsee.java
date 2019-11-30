package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;


public class Invsee extends SimpleCommand {

    public Invsee() {
        super("invsee");
    }

    @Override
    protected void onCommand() {
        Player p = (Player) sender;
        if (args.length < 1) {
            if (p.hasPermission("vrn.invsee.self")) {
                p.openInventory(p.getInventory());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing your inventory."));
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        } else {
            Player a = Bukkit.getPlayerExact(args[0]);
            if (p.hasPermission("vrn.invsee.others")) {
                if (a != null) {
                    p.openInventory(a.getInventory());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing &a" + a.getName() + "&7's inventory."));
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
    }
}
