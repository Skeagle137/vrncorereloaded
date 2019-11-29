package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class Craft extends SimpleCommand {

    public Craft() {
        super("heal");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (p.hasPermission("vrn.craft")) {
            p.openWorkbench(null, true);
        } else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}
