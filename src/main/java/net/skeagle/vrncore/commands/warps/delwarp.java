package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.Warp;
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
        setPermission(null);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Warp w = WarpManager.getInstance().getWarp(args[0]);
        if (w == null)
            returnTell("&cThat warp does not exist.");
        if (!w.getOwner().equals(getPlayer().getUniqueId()))
            checkPerm("vrn.delwarp.others");
        else
            checkPerm("vrn.delwarp.self");
        if (WarpManager.getInstance().delWarp(args[0]))
            returnTell("&7Warp &a" + args[0] + "&7 successfully deleted.");
        else
            returnTell("&cSomething went wrong when trying to delete the warp.");
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1)
            return completeLastWord(hasPerm("vrn.delwarp.others") ?
                    WarpManager.getInstance().getWarpNames() : WarpManager.getInstance().getWarpsOwnedByPlayer(getPlayer()));
        return completeLastWord();
    }
}
