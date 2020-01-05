package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class setwarp extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public setwarp(final Resources r) {
        super("setwarp");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a warp.");
        setPermission("vrn.setwarp");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.setValues(getPlayer(), "warps.", args[0], false);
    }
}
