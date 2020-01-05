package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TPhere extends SimpleCommand {

    public TPhere() {
        super("tphere");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Teleport a player to your location.");
        setPermission("vrn.tphere");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player a = findPlayer(args[0], VRNcore.noton);
        say(a, "Teleporting...");
        a.teleport(getPlayer().getLocation());
    }
}
