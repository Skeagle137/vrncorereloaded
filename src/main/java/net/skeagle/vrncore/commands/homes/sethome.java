package net.skeagle.vrncore.commands.homes;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.Resources;
import net.skeagle.vrncore.utils.WarpsHomesUtil;
import org.mineacademy.fo.command.SimpleCommand;

public class sethome extends SimpleCommand {
    private Resources r;
    private WarpsHomesUtil util;

    public sethome(final Resources r) {
        super("sethome");
        this.r = r;
        util = new WarpsHomesUtil(r);
        setMinArguments(1);
        setUsage("<name>");
        setDescription("Create a Home.");
        setPermission("vrn.sethome");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        util.setValues(getPlayer(), "homes." + getPlayer().getUniqueId() + ".", args[0], true);
    }
}
