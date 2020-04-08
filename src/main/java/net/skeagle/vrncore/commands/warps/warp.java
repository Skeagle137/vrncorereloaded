package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpsManager;
import net.skeagle.vrncore.utils.storage.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class warp extends SimpleCommand {

    public warp() {
        super("warp");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Teleport to the specified warp.");
        setPermission("vrn.warp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final WarpsManager man = WarpsResource.getInstance().getWarp(args[0]);
        if (man != null) {
            if (!man.teleWarp(getPlayer())) {
                WarpsResource.getInstance().delWarp(args[0]);
                returnTell("&cLocation not found, deleted warp.");
            }
            returnTell("&7Teleporting...");
        }
        returnTell("&cThat warp does not exist.");
    }

    @Override
    protected List<String> tabComplete() {
        final List<String> names = WarpsResource.getInstance().getWarpNames();
        if (!names.isEmpty()) {
            if (args.length == 1) return completeLastWord(names);
        }
        return completeLastWord("");
    }
}
