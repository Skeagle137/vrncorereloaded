package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Echest extends SimpleCommand {

    public Echest() {
        super("echest");
        setDescription("Opens a player's ender chest.");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args.length < 1) {
            hasPerm("vrn.echest.self");
            p.openInventory(p.getEnderChest());
            say(p, "Now showing your inventory.");
            return;
        }
        Player a = findPlayer(args[0], VRNcore.noton);
        hasPerm("vrn.echest.others");
        p.openInventory(a.getEnderChest());
        say(p, "Now showing &a" + a.getName() + "&7's ender chest.");
    }
}
