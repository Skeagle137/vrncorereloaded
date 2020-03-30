package net.skeagle.vrncore.commands.warps;

import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class warps extends SimpleCommand {
    private final Resources r;
    private final WarpsHomesUtil util;

    public warps(final Resources r) {
        super("warps");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setDescription("List all available warps.");
        setPermission("vrn.warps");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (r.getWarps().get("warps.") != null) {
            say(getPlayer(), "&7Currently showing a list of &a"
                    + util.returnArray().size() + "&7 warp(s): &a"
                    + String.join("&7,&a ", util.returnArray()) + "&7.");
        } else {
            say(getPlayer(),"&cThere are currently no warps available.");
        }
    }
}

