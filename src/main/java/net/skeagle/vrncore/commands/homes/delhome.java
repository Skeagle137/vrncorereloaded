package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.homes.HomesManager;
import net.skeagle.vrncore.utils.homes.HomesResource;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

public class delhome extends SimpleCommand {

    public delhome() {
        super("delhome");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a home.");
        setPermission("norsehomes.delhome");
        setPermissionMessage("&cYou do not have permission.");
    }

    @Override
    public void onCommand() {
        checkConsole();
        final HomesManager man = HomesResource.getInstance().getHome(getPlayer().getUniqueId());
        if (!man.delHome(args[0]))
            returnTell("&cThat home does not exist.");
        returnTell("&7Home &a" + args[0] + "&7 successfully deleted.");
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
