package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.GUIs.TrailsGUI;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Trails extends SimpleCommand {

    public Trails() {
        super("trails|trail");
        setDescription("A menu for selecting and customizing arrow and player trails.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player p = (args.length != 0 ? findPlayer(args[0], VRNUtil.noton) : getPlayer());
        if (p != getPlayer())
            checkPerm("vrn.trails.others");
        else
            checkPerm("vrn.trails.self");
        new TrailsGUI(p).displayTo(getPlayer());
    }
}
