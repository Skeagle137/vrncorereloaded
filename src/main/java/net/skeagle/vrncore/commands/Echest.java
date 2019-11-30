package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Echest extends SimpleCommand {

    public Echest() {
        super("echest");
    }

    @Override
    protected void onCommand() {
        Player p = (Player) sender;
        if (args.length < 1) {
            if (p.hasPermission("vrn.echest.self")) {
                p.openInventory(p.getEnderChest());
                p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing your inventory."));
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        } else {
            Player a = Bukkit.getPlayerExact(args[0]);
            if (p.hasPermission("vrn.echest.others")) {
                if (a != null) {
                    p.openInventory(a.getEnderChest());
                    p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn + "Now showing &a" + a.getName() + "&7's ender chest."));
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
    }
}
