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
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            checkPerm("vrn.echest.self");
            p.openInventory(p.getEnderChest());
            say(p, "Now showing your inventory.");
            return;
        }
        final Player a = findPlayer(args[0], VRNUtil.noton);
        checkPerm("vrn.echest.others");
        p.openInventory(a.getEnderChest());
        say(p, "Now showing &a" + a.getName() + "&7's ender chest.");
    }
}
