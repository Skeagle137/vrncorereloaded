package net.skeagle.vrncore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class Rename implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        String s = String.join(" ", args);
        if (p.hasPermission("vrn.rename")) {
            ItemStack is = p.getInventory().getItemInMainHand();
            if (is != null) {
            ItemMeta im = is.getItemMeta();
            im.setDisplayName(VRNcore.color(s));
            is.setItemMeta(im);
                p.sendMessage(VRNcore.vrn + "Item successfully renamed.");
            }
            else {
                p.sendMessage(VRNcore.no + "You must be holding an item in your main hand.");
            }
        }
        else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
    }
}
