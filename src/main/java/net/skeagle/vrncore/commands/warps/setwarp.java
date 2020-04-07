package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

public class setwarp extends SimpleCommand {

    public setwarp() {
        super("setwarp");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a warp.");
        setPermission("vrn.setwarp");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (WarpsResource.getInstance().setWarp(getPlayer(), args[0]))
            returnTell("&7Warp set, teleport to it with &a/warp " + args[0] + "&7.");
        returnTell("&cA warp with that name already exists.");
    }
}
