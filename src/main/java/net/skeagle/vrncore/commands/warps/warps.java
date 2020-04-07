package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.warps.WarpsResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class warps extends SimpleCommand {

    public warps() {
        super("warps");
        setDescription("List all available warps.");
        setPermission("vrn.warps");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final List<String> names = WarpsResource.getInstance().getWarpNames();
        if (!names.isEmpty()) {
            returnTell("&7Currently showing a list of &a" + names.size() +
                    "&7 warp(s): &a" + String.join("&7,&a ", names) + "&7.");
        }
        returnTell("&cThere are no warps available.");
    }
}

