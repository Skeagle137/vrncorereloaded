package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.homes.HomesManager;
import net.skeagle.vrncore.utils.homes.HomesResource;
import org.mineacademy.fo.command.SimpleCommand;


public class sethome extends SimpleCommand {

    public sethome() {
        super("sethome");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a Home.");
        setPermission("vrn.sethome");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final HomesManager man = HomesResource.getInstance().getHome(getPlayer().getUniqueId());
        if (!man.setHome(args[0], getPlayer().getLocation()))
            returnTell("&cA home with that name already exists.");
        returnTell("&7Home set, teleport to it with &a/home " + args[0] + "&7.");
    }
}
