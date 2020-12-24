package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.GUIs.WarpsGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import org.mineacademy.fo.command.SimpleCommand;

public class warps extends SimpleCommand {

    public warps() {
        super("warps");
        setDescription("List all available warps.");
        setPermission("vrn.warps");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (!WarpManager.getInstance().getWarpNames().isEmpty()) {
            new WarpsGUI().displayTo(getPlayer());
            return;
        }
        returnTell("&cThere are no warps available.");
    }
}

