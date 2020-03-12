package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Mute extends SimpleCommand implements Listener {

    public Mute() {
        super("mute");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Mute a player.");
        setPermission("vrn.mute");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final PlayerCache cache = PlayerCache.getCache(a);
        cache.setMuted(!cache.isMuted());
        say(a, "You are " + (cache.isMuted() ? "now" : "no longer") + " muted.");
        say(getSender(), "&a" + a.getName() + " &7is " + (cache.isMuted() ? "now" : "no longer") + " muted.");
    }
}
