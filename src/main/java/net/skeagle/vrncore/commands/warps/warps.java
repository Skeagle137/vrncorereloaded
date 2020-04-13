package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.GUIs.WarpsGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

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
        final List<String> names = WarpsResource.getInstance().getWarpNames();
        if (!names.isEmpty()) {
            new WarpsGUI().displayTo(getPlayer());
            return;
        }
        returnTell("&cThere are no warps available.");
    }
}

