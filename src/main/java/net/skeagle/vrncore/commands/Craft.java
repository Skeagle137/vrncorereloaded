package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class Craft extends SimpleCommand {

    public Craft() {
        super("craft");
        setDescription("Opens a crafting table interface.");
        setPermission("vrn.craft");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        getPlayer().openWorkbench(null, true);
    }
}
