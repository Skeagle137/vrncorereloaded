package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Rename extends SimpleCommand {

    public Rename() {
        super("rename");
        setUsage("<new name>");
        setDescription("Renames an item in hand.");
        setPermission("vrn.rename");
        setPermissionMessage(VRNUtil.noperm);

    }

    @Override
    public void onCommand() {
        checkConsole();
        final String s = String.join(" ", args);
        final ItemStack is = getPlayer().getInventory().getItemInMainHand();
        final ItemMeta im = is.getItemMeta();
        if (im == null) {
            say(getPlayer(), "&cYou must have an item in your hand.");
            return;
        }
        im.setDisplayName(color(s));
        is.setItemMeta(im);
        say(getPlayer(), "Item successfully renamed.");
    }
}
