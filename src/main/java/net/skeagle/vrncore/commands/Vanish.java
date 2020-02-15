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
            Player p = getPlayer();
            hasPerm("vrn.vanish.self");
            PlayerCache cache = PlayerCache.getCache(p);
            cache.setVanished(!cache.isVanished());
            for (Player pl : Bukkit.getOnlinePlayers()) {
                if (!cache.isVanished()) {
                    pl.hidePlayer(VRNcore.getInstance(), p);
                    return;
                }
                pl.showPlayer(VRNcore.getInstance(), p);
            }
            say(p, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
            return;
        }
        hasPerm("vrn.vanish.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        PlayerCache cache = PlayerCache.getCache(a);
        cache.setVanished(!cache.isVanished());
        for (Player pl : Bukkit.getOnlinePlayers()) {
            if (!cache.isVanished()) {
                pl.hidePlayer(VRNcore.getInstance(), a);
                return;
            }
            pl.showPlayer(VRNcore.getInstance(), a);
        }
        say(a, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
        say(getSender(), "Vanish " + (cache.isVanished() ? "enabled" : "disabled") + " for &a" + a.getName() + "&7.");
    }
}


