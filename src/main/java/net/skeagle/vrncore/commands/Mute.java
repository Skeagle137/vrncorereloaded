package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Mute extends SimpleCommand implements Listener {

    private static ArrayList<String> mute = new ArrayList<String>();

    public Mute() {
        super("mute");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Mute a player.");
        setPermission("vrn.mute");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        Player a = findPlayer(args[0], VRNcore.noton);
        if (!mute.contains(a.getName())) {
            mute.add(a.getName());
            say(a,"You are now muted.");
            say(getSender(),"&a" + a.getName() + " &7is now muted.");
        } else {
            mute.remove(a.getName());
           say(a,"You are no longer muted.");
            say(getSender(),"&a" + a.getName() + " &7is no longer muted.");
        }
    }
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (mute.contains(p.getName())) {
            e.setCancelled(true);
            say(p,"&cYou are muted. You cannot chat.");
        }
    }
}
