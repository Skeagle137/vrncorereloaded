package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;

public class Mute implements CommandExecutor, Listener {
    private static ArrayList<String> mute = new ArrayList<String>();

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        if (args.length == 0) {
            p.sendMessage(VRNcore.sp);
        } else if (args.length == 1) {
            if (p.hasPermission("vrn.mute")) {
                Player a = Bukkit.getPlayerExact(args[0]);
                if (a != null) {
                    if (!mute.contains(a.getName())) {
                        mute.add(a.getName());
                        a.sendMessage(VRNcore.vrn + "You are now muted.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + " &7is now muted."));
                    } else {
                        mute.remove(a.getName());
                        a.sendMessage(VRNcore.vrn + "You are no longer muted.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + " &7is no longer muted."));
                    }
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }
    @EventHandler
    public void onAsyncPlayerChat(AsyncPlayerChatEvent e) {
        Player p = e.getPlayer();
        if (mute.contains(p.getName())) {
            e.setCancelled(true);
            p.sendMessage(VRNcore.no + "You are muted. You cannot chat.");
        }
    }
}
