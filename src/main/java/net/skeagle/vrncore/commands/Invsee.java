package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
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
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        final Player a = findPlayer(args[0], VRNUtil.noton);
        getPlayer().openInventory(a.getInventory());
        say(getPlayer(),"Now showing &a" + a.getName() + "&7's inventory.");
    }
}
