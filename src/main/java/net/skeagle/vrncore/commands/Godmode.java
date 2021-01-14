package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Godmode extends SimpleCommand implements Listener {

    public Godmode() {
        super("god");
        setDescription("Make yourself or another player invulnerable.");
        setUsage("[player]");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        final Player p = getPlayer();
        if (args.length < 1) {
            final PlayerData data = PlayerManager.getData(p);
            hasPerm("vrn.god.self");
            data.setGodmode(!data.getGodmode());
            say(p, "You are " + (data.getGodmode() ? "now" : "no longer") + " invulnerable.");
            return;
        }
        hasPerm("vrn.god.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final PlayerData data = PlayerManager.getData(a);
        data.setGodmode(!data.getGodmode());
        say(a, "You are " + (data.getGodmode() ? "now" : "no longer") + " invulnerable.");
        say(p, "&a" + a.getName() + " &7is " + (data.getGodmode() ? "now" : "no longer") + " invulnerable.");
    }
}