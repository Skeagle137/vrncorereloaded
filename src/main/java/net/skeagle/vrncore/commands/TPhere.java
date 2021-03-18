package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class TPhere extends SimpleCommand {

    public TPhere() {
        super("tphere");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Teleport a player to your location.");
        setPermission("vrn.tphere");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        final Player a = findPlayer(args[0], VRNUtil.noton);
        say(a, "Teleporting...");
        a.teleport(getPlayer().getLocation());
    }
}
