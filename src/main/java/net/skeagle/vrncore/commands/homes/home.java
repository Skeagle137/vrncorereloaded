package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.Home;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

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
        Home h = HomeManager.getInstance().getHome(args[0], getPlayer());
        if (h == null) returnTell("&cThat home does not exist.");
        getPlayer().teleport(h.getLocation());
        returnTell("&7Teleporting...");
    }

    @Override
    protected List<String> tabComplete() {
        if (HomeManager.getInstance().getHomes(getPlayer()).size() != 0)
            if (args.length == 1) return completeLastWord(HomeManager.getInstance().getHomeNames(getPlayer()));
        return completeLastWord("");
    }
}
