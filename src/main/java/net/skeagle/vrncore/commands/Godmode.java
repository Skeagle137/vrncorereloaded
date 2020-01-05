package net.skeagle.vrncore.commands;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Godmode extends SimpleCommand implements Listener {
    private static ArrayList<String> god = new ArrayList<String>();

    public Godmode() {
        super("god");
        setDescription("Make yourself or another player invulnerable.");
        setUsage("[player]");
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args.length < 1) {
            hasPerm("vrn.god.self");
            if (!god.contains(p.getName())) {
                god.add(p.getName());
                say(p, "You are now invulnerable.");
            } else {
                god.remove(p.getName());
                say(p, "You are no longer invulnerable.");
            }
            return;
        }
        hasPerm("vrn.god.others");
        Player a = findPlayer(args[0], VRNcore.noton);
        if (!god.contains(a.getName())) {
            god.add(a.getName());
            say(a, "You are now invulnerable.");
            say(p, "&a" + a.getName() + " &7is now invulnerable.");
        } else {
            god.remove(a.getName());
            say(a, "You are no longer invulnerable.");
            say(p, "&a" + a.getName() + " &7is no longer invulnerable.");
        }
    }

    @EventHandler
    public void onGodDamage(EntityDamageEvent e) {
        if (e.getEntity() instanceof Player) {
            Player p = (Player) e.getEntity();
            if (god.contains(p.getName())) {
                e.setCancelled(true);
            }
        }
    }
}