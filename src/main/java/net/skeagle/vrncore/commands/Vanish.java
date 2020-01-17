package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Vanish extends SimpleCommand implements Listener {

    public Vanish() {
        super("vanish");
        setUsage("<player>");
        setDescription("Hide yourself or another player from other players.");
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
                pl.hidePlayer(VRNcore.getInstance(), p);
            }
            say(p, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
            return;
        }
        hasPerm("vrn.vanish.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        PlayerCache cache = PlayerCache.getCache(a);
        cache.setVanished(!cache.isVanished());
        for (Player pl : Bukkit.getOnlinePlayers()) {
            pl.hidePlayer(VRNcore.getInstance(), a);
        }
        say(a, "Vanish " + (cache.isVanished() ? "enabled." : "disabled."));
        say(getSender(), "Vanish " + (cache.isVanished() ? "enabled" : "disabled") + " for &a" + a.getName() + "&7.");
    }
}


