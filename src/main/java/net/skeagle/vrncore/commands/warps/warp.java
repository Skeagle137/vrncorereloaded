package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.warps.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class warp extends SimpleCommand {
    private final Resources r;
    private final WarpsHomesUtil util;

    public warp(final Resources r) {
        super("warp");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Teleport to the specified warp.");
        setPermission("vrn.warp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.teleportToLoc(getPlayer(), args[0]);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            if (r.getWarps().get("warps.") != null) {
                return completeLastWord(util.returnArray());
            } else {
                return completeLastWord("");
            }
        }
        return new ArrayList<>();
    }
}
