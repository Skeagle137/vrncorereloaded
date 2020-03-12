package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.GGiveGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class GGive extends SimpleCommand {

    public GGive() {
        super("ggive");
        setDescription("open a gui inventory for a quick way to obtain special items.");
        setPermission("vrn.ggive");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        new GGiveGUI().displayTo(getPlayer());
    }
}
