package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.listeners.GUIgive;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

public class openGGive extends SimpleCommand {

    public openGGive() {
        super("ggive");
    }

    @Override
    public void onCommand() {
        Player p = (Player) sender;
        if (p.hasPermission("vrn.ggive")) {
            GUIgive g = new GUIgive();
            g.give(p);
        } else {
            p.sendMessage(VRNcore.noperm);
        }
    }
}
