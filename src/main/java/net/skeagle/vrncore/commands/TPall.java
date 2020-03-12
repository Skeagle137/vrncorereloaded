package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TPall extends SimpleCommand {

    public TPall() {
        super("tpall");
        setDescription("Teleport everyone online to your location.");
        setPermission("vrn.tpall");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (pl != getPlayer()) {
                say(pl,"Teleporting...");
                pl.teleport(getPlayer().getLocation());
            }
        }
        say(getPlayer(),"Teleported all players.");
    }
}
