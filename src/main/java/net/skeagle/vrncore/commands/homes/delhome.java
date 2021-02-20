package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.homes.HomeManager;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class delhome extends SimpleCommand {

    public delhome() {
        super("delhome");
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a home.");
        setPermission("vrn.delhome");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (!HomeManager.getInstance().delHome(args[0], getPlayer())) {
            say(getPlayer(), "&cThat home does not exist.");
            return;
        }
        say(getPlayer(), "&7Home &a" + args[0] + "&7 successfully deleted.");
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) return completeLastWord(HomeManager.getInstance().getHomeNames(getPlayer()));
        return completeLastWord();
    }
}
