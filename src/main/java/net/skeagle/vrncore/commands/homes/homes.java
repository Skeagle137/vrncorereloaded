package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.GUIs.HomesGUI;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class homes extends SimpleCommand {

    public homes() {
        super("homes");
        setDescription("List all available homes.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player p = (args.length != 0 ? findPlayer(args[0], VRNUtil.noton) : getPlayer());
        if (p != getPlayer())
            checkPerm("vrn.homes.self");
        else
            checkPerm("vrn.homes.others");
        if (!HomeManager.getInstance().getHomeNames(p).isEmpty()) {
            new HomesGUI(p).displayTo(getPlayer());
            return;
        }
        say(getPlayer(), "&c" + (p != getPlayer() ? p.getName() + " does" : "You do") + " not have any homes available.");
    }
}

