package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class delwarp extends SimpleCommand {

    public delwarp() {
        super("delwarp");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a warp.");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (WarpManager.getInstance().delWarp(args[0])) {
            if (!WarpManager.getInstance().getWarp(args[0]).getOwner().equals(getPlayer().getUniqueId()))
                checkPerm("vrn.delwarp.others");
            else
                checkPerm("vrn.delwarp.self");
            returnTell("&7Warp &a" + args[0] + "&7 successfully deleted.");
        }
        returnTell("&cThat warp does not exist.");
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(hasPerm("vrn.delwarp.others") ?
                    WarpManager.getInstance().getWarpNames() : WarpManager.getInstance().getWarpsOwnedByPlayer(getPlayer()));
        return completeLastWord();
    }
}
