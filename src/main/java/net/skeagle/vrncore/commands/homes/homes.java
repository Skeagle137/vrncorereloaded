package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.homes.HomesManager;
import net.skeagle.vrncore.utils.homes.HomesResource;
import org.mineacademy.fo.command.SimpleCommand;

public class homes extends SimpleCommand {

    public homes() {
        super("homes");
        setDescription("List all available homes.");
        setPermission("norsehomes.homes");
        setPermissionMessage("&cYou do not have permission.");
    }

    //comma separated list of homes
    @Override
    public void onCommand() {
        checkConsole();
        final HomesManager man = HomesResource.getInstance().getHome(getPlayer().getUniqueId());
        if (man.homeNames().size() != 0) {
            returnTell("&7Currently showing a list of &a" + man.homeNames().size() +
                    "&7 home(s): &a" + String.join("&7,&a ", man.homeNames()) + "&7.");
        }
        returnTell("&cYou do not have any homes available.");
    }
}

