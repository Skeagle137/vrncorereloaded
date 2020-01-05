package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.ExpTradeGUI;
import net.skeagle.vrncore.VRNcore;
import org.mineacademy.fo.command.SimpleCommand;

public class Exptrade extends SimpleCommand {


    public Exptrade() {
        super("exptrade|expt");
        setDescription("Allows Trading items for experience points.");
        setPermission("vrn.exptrade");
        setPermissionMessage(VRNcore.noperm);
    }

    public void onCommand() {
        ExpTradeGUI exp = new ExpTradeGUI(getPlayer());
        exp.open(getPlayer());
    }
}
