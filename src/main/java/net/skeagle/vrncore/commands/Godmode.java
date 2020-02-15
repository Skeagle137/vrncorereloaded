package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Godmode extends SimpleCommand implements Listener {

    public Godmode() {
        super("god");
        setDescription("Make yourself or another player invulnerable.");
        setUsage("[player]");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args.length < 1) {
            PlayerCache cache = PlayerCache.getCache(p);
            hasPerm("vrn.god.self");
            cache.setGodmode(!cache.isGodmode());
            say(p, "You are " + (cache.isGodmode() ? "now" : "no longer") + " invulnerable.");
            return;
        }
        hasPerm("vrn.god.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        PlayerCache cache = PlayerCache.getCache(a);
        cache.setGodmode(!cache.isGodmode());
        say(a, "You are " + (cache.isGodmode() ? "now" : "no longer") + " invulnerable.");
        say(p, "&a" + a.getName() + " &7is " + (cache.isGodmode() ? "now" : "no longer") + " invulnerable.");
    }
}