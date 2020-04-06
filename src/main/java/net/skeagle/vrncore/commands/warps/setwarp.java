package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.warps.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class setwarp extends SimpleCommand {
    private final Resources r;
    private final WarpsHomesUtil util;

    public setwarp(final Resources r) {
        super("setwarp");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a warp.");
        setPermission("vrn.setwarp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.setValues(getPlayer(), args[0]);
    }
}
