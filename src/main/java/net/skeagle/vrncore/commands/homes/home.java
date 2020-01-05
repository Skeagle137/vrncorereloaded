package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class home extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public home(final Resources r) {
        super("home");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Teleport to the specified home.");
        setPermission("vrn.home");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.teleportToLoc(getPlayer(), "homes." + getPlayer().getUniqueId() + ".", args[0], true);
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 1) {
            if (this.r.getWarps().get("homes." + getPlayer().getUniqueId()) != null) {
                return completeLastWord(util.returnArray("homes." + getPlayer().getUniqueId()));
            } else {
                return completeLastWord("");
            }
        }
        return new ArrayList<>();
    }
}
