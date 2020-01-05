package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class warp extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public warp(final Resources r) {
        super("warp");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Teleport to the specified warp.");
        setPermission("vrn.warp");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.teleportToLoc(getPlayer(), "warps.", args[0], false);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            if (this.r.getWarps().get("warps.") != null) {
                return completeLastWord(util.returnArray("warps."));
            } else {
                return completeLastWord("");
            }
        }
        return new ArrayList<>();
    }
}
