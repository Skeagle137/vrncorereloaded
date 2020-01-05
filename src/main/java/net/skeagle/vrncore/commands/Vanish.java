package net.skeagle.vrncore.commands;

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

    private ArrayList<Player> vanished = new ArrayList<Player>();

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
            if (!vanished.contains(p)) {
                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.hidePlayer(VRNcore.getInstance(), p);
                }
                vanished.add(p);
                say(p, "Vanish enabled.");
            } else {

                for (Player pl : Bukkit.getOnlinePlayers()) {
                    pl.showPlayer(VRNcore.getInstance(), p);
                }
                vanished.remove(p);
                say(p, "Vanish disabled.");
            }
            return;
        }
        Player p = getPlayer();
        hasPerm("vrn.vanish.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        if (!vanished.contains(a)) {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.hidePlayer(VRNcore.getInstance(), a);
            }
            vanished.add(a);
            say(a, "Vanish enabled.");
            say(p, "Vanish enabled for &a" + a.getName() + ".");
        } else {
            for (Player pl : Bukkit.getOnlinePlayers()) {
                pl.showPlayer(VRNcore.getInstance(), a);
            }
            vanished.remove(a);
            say(a, "Vanish disabled.");
            say(p, "Vanish disabled for &a" + a.getName() + ".");
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Player p : vanished) {
            e.getPlayer().hidePlayer(VRNcore.getInstance(), p);
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        vanished.remove(e.getPlayer());
    }
}


