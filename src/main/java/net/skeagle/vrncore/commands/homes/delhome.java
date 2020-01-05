package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

public class delhome extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public delhome(final Resources r) {
        super("delhome");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Remove a home.");
        setPermission("vrn.delhome");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.delValues(getPlayer(), "homes." + getPlayer().getUniqueId() + ".", args[0], true);
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
