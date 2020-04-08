package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class delwarp extends SimpleCommand {

    public delwarp() {
        super("delwarp");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a warp.");
        setPermission("vrn.delwarp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (WarpsResource.getInstance().delWarp(args[0]))
            returnTell("&7Warp &a" + args[0] + "&7 successfully deleted.");
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
