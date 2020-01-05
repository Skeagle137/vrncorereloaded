package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class delwarp extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public delwarp(final Resources r) {
        super("delwarp");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a warp.");
        setPermission("vrn.delwarp");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.delValues(getPlayer(), "warps.", args[0], false);
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
