package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class homes extends SimpleCommand {

    public homes() {
        super("homes");
        setDescription("List all available homes.");
        setPermission("vrn.homes");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player a = null;
        if (args.length != 0)
            a = findPlayer(args[0], VRNUtil.noton);
        if (!HomeManager.getInstance().getHomeNames(a != null ? a : getPlayer()).isEmpty()) {
            new HomesGUI(a != null ? a : getPlayer()).displayTo(getPlayer());
            return;
        }
        returnTell("&c" + (a != null ? a.getName() + " does" : "You do") + " not have any homes available.");
    }
}

