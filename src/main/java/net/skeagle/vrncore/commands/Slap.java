package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Slap extends SimpleCommand {

    public Slap() {
        super("slap");
        setDescription("Slap a player.");
        setPermission("vrn.slap");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player a = findPlayer(args[0], VRNUtil.noton);
        a.setHealth(0);
        say(getSender(), "You rekt &a" + a.getName() + "&7.");
    }
}

