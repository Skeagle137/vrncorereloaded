package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;


public class Invsee extends SimpleCommand {

    public Invsee() {
        super("invsee");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("View another player's inventory.");
        setPermission("vrn.invsee");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        Player a = findPlayer(args[0], VRNcore.noton);
        getPlayer().openInventory(a.getInventory());
        say(getPlayer(),"Now showing &a" + a.getName() + "&7's inventory.");
    }
}
