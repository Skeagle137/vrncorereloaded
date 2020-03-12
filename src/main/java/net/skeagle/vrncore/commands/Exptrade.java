package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.ExpTradeGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class Exptrade extends SimpleCommand {


    public Exptrade() {
        super("exptrade|expt");
        setDescription("Allows Trading items for experience points.");
        setPermission("vrn.exptrade");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        final ExpTradeGUI exp = new ExpTradeGUI(getPlayer());
        exp.open(getPlayer());
    }
}
