package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.command.SimpleCommand;

public class Rename extends SimpleCommand {

    public Rename() {
        super("rename");
    }

    @Override
    public void onCommand() {
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
    }
}
