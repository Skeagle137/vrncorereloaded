package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.VRNcore;
import org.mineacademy.fo.command.SimpleCommand;

public class Trails extends SimpleCommand {

    public Trails() {
        super("trails|trail");
        setDescription("A menu for selecting and customizing arrow and player trails.");
        setPermission("vrn.trails");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        new TrailsGUI().displayTo(getPlayer());
    }
}
