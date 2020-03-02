package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Slap extends SimpleCommand {

    public Slap() {
        super("slap");
        setDescription("Slap a player. (This is a christian minecraft server, we dont kill)");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1) {
            checkConsole();
            hasPerm("vrn.slap.self");
            getPlayer().setHealth(0);
            return;
        }
        hasPerm("vrn.slap.others");
        final Player a = findPlayer(args[0], VRNcore.noton);
        a.setHealth(0);
        say(getSender(), "You rekt &a" + a.getName() + "&7.");
    }
}

