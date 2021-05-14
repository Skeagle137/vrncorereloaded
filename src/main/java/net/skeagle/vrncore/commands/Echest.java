package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Echest extends SimpleCommand {

    public Echest() {
        super("echest");
        setDescription("Opens a player's ender chest.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1)
            checkConsole();
        final Player p = args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton);
        checkPerm("vrn.echest." + (args.length < 1 ? "self" : "others"));
        p.openInventory(p.getEnderChest());
        say(getSender(), args.length < 1 ? "Now showing your ender chest." : "Now showing &a" + p.getName() + "&7's ender chest.");
    }
}
