package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Smite extends SimpleCommand {

    public Smite() {
        super("smite");
        setMinArguments(1);
        setUsage("<player|*>");
        setDescription("strike a player by summoning lightning at their location.");
        setPermission("vrn.smite");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        final Player a = findPlayer(args[0], VRNUtil.noton);
        if (args[0].equalsIgnoreCase("*")) {
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                pl.getWorld().strikeLightning(pl.getLocation());
            }
            say(getSender(), "smited all players.");
            return;
        }
        a.getWorld().strikeLightning(a.getLocation());
        say(getSender(), "smited &a" + a.getName() + "&7.");
    }
}
