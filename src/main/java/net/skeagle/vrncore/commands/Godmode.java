package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Godmode extends SimpleCommand implements Listener {

    public Godmode() {
        super("god");
        setDescription("Make yourself or another player invulnerable.");
        setUsage("[player]");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 1)
            checkConsole();
        final VRNPlayer p = new VRNPlayer(args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton));
        checkPerm("vrn.god." + (args.length < 1 ? "self" : "others"));
        p.setGodmode(!p.isGodmode());
        say(p, "You are " + (p.isGodmode() ? "now" : "no longer") + " invulnerable.");
        if (args.length < 1 || p.getPlayer() == getSender()) return;
        say(getSender(), "&a" + p.getName() + " &7is " + (p.isGodmode() ? "now" : "no longer") + " invulnerable.");
    }
}