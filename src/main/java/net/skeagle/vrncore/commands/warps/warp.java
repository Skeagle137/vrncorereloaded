package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.warps.Warp;
import net.skeagle.vrncore.utils.storage.warps.WarpManager;
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
        Warp w = WarpManager.getInstance().getWarp(args[0]);
        if (w == null) returnTell("&cThat warp does not exist.");
        getPlayer().teleport(w.getLocation());
        returnTell("&7Teleporting...");
    }

    @Override
    protected List<String> tabComplete() {
        final List<String> names = WarpManager.getInstance().getWarpNames();
        if (!names.isEmpty())
            if (args.length == 1) return completeLastWord(names);
        return completeLastWord("");
    }
}
