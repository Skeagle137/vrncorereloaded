package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Vanish extends SimpleCommand implements Listener {

    public Vanish() {
        super("vanish");
        setUsage("<player>");
        setDescription("Hide yourself or another player from other players.");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final Player p = getPlayer();
            hasPerm("vrn.vanish.self");
            final PlayerCache cache = PlayerCache.getCache(p);
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (!cache.isVanished()) {
                    pl.hidePlayer(VRNcore.getInstance(), p);
                    return;
                }
                pl.showPlayer(VRNcore.getInstance(), p);
            }
            cache.setVanished(!cache.isVanished());
            say(p, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
            return;
        }
        hasPerm("vrn.vanish.others");
        final Player a = findPlayer(args[0], VRNcore.noton);
        final PlayerCache cache = PlayerCache.getCache(a);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!cache.isVanished()) {
                pl.hidePlayer(VRNcore.getInstance(), a);
                return;
            }
            pl.showPlayer(VRNcore.getInstance(), a);
        }
        cache.setVanished(!cache.isVanished());
        say(a, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
        say(getSender(), "Vanish " + (cache.isVanished() ? "enabled" : "disabled") + " for &a" + a.getName() + "&7.");
    }
}


