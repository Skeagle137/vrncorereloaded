package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomesManager;
import net.skeagle.vrncore.utils.storage.homes.HomesResource;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class homes extends SimpleCommand {

    public homes() {
        super("homes");
        setDescription("List all available homes.");
        setPermission("vrn.homes");
        setPermissionMessage(VRNUtil.noperm);
    }

    //comma separated list of homes
    @Override
    public void onCommand() {
        checkConsole();
        Player a = null;
        if (!(args.length < 1)) {
            a = findPlayer(args[0], VRNUtil.noton);
        }
        final HomesManager man = HomesResource.getInstance().getHome(a != null ? a.getUniqueId() : getPlayer().getUniqueId());
        if (!man.homeNames().isEmpty()) {
            new HomesGUI(a != null ? a : getPlayer()).displayTo(getPlayer());
            return;
        }
        say(getPlayer(), "&c" + (a != null ? a.getName() + " does" : "You do") + " not have any homes available.");
    }
}

