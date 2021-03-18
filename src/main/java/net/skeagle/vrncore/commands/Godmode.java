package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
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
        checkConsole();
        if (args.length < 1) {
            final VRNPlayer p = new VRNPlayer(getPlayer());
            checkPerm("vrn.god.self");
            p.setGodmode(!p.isGodmode());
            say(p, "You are " + (p.isGodmode() ? "now" : "no longer") + " invulnerable.");
            return;
        }
        checkPerm("vrn.god.others");
        final VRNPlayer a = new VRNPlayer(findPlayer(args[0], VRNUtil.noton));
        a.setGodmode(!a.isGodmode());
        say(a, "You are " + (a.isGodmode() ? "now" : "no longer") + " invulnerable.");
        say(getPlayer(), "&a" + a.getName() + " &7is " + (a.isGodmode() ? "now" : "no longer") + " invulnerable.");
    }
}