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

public class Vanish extends SimpleCommand implements Listener {

    private ArrayList<Player> vanished = new ArrayList<Player>();

    private VRNcore plugin;

    public Vanish(VRNcore vrncore) {
        super("vanish");
        plugin = vrncore;
    }

    @Override
    public void onCommand() {
        checkConsole();
        Player p = (Player) sender;
        if (args.length == 0) {
            if (p.hasPermission("vrn.vanish.self")) {
                if (!vanished.contains(p)) {

                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.hidePlayer(this.plugin, p);
                    }
                    vanished.add(p);
                    p.sendMessage(VRNcore.vrn + "Vanish enabled.");
                } else {

                    for (Player pl : Bukkit.getOnlinePlayers()) {
                        pl.showPlayer(this.plugin, p);
                    }
                    vanished.remove(p);
                    p.sendMessage(VRNcore.vrn + "Vanish disabled.");
                }
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (args.length == 1) {
            if (p.hasPermission("vrn.vanish.others")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    if (!vanished.contains(a)) {

                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.hidePlayer(this.plugin, a);
                        }
                        vanished.add(a);
                        a.sendMessage(VRNcore.vrn + "Vanish enabled.");
                        p.sendMessage(VRNcore.vrn + "Vanish enabled for &a" + a.getName() + ".");
                    } else {

                        for (Player pl : Bukkit.getOnlinePlayers()) {
                            pl.showPlayer(this.plugin, a);
                        }
                        vanished.remove(a);
                        a.sendMessage(VRNcore.vrn + "Vanish disabled.");
                        p.sendMessage(VRNcore.vrn + "Vanish disabled for &a" + a.getName() + ".");
                    }
                }
                else {
                    p.sendMessage(VRNcore.noton);
                }
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        for (Player p : vanished) {
            e.getPlayer().hidePlayer(this.plugin, p);
        }
    }


    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent e) {
        vanished.remove(e.getPlayer());
    }
}


