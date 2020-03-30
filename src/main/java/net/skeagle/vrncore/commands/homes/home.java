package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.homes.HomesManager;
import net.skeagle.vrncore.utils.homes.HomesResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class home extends SimpleCommand {

    public home() {
        super("home");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Teleport to the specified home.");
        setPermission("vrn.home");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final HomesManager man = HomesResource.getInstance().getHome(getPlayer().getUniqueId());
        if (!man.teleHome(getPlayer(), args[0])) {
            say(getPlayer(), "&cThat home does not exist.");
            return;
        }
        say(getPlayer(), "&7Teleporting...");
    }

    /*
    tab complete, because who is going to remember
    each of those 100 homes they set?
    */
    @Override
    protected List<String> tabComplete() {
        final HomesManager man = HomesResource.getInstance().getHome(getPlayer().getUniqueId());
        if (man.homeNames().size() != 0) {
            if (args.length == 1) return completeLastWord(man.homeNames());
        }
        return completeLastWord("");
    }
}
