package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
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
        setPermissionMessage(VRNcore.noperm);

    }

    @Override
    public void onCommand() {
        checkConsole();
        String s = String.join(" ", args);
        ItemStack is = getPlayer().getInventory().getItemInMainHand();
        ItemMeta im = is.getItemMeta();
        im.setDisplayName(color(s));
        is.setItemMeta(im);
        say(getPlayer(), "Item successfully renamed.");
    }
}
