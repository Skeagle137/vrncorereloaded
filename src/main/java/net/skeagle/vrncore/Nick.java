package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class Nick implements CommandExecutor, Listener {
    private VRNcore plugin;

    Nick(VRNcore vrncore) {
        plugin = vrncore;
    }

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        Player p = (Player) sender;
        StringBuilder sb = new StringBuilder();
        for (int i = 1; i < args.length; i++) {
            sb.append(args[i]).append(" ");
        }
        String nick = sb.toString();

        nick = nick.substring(0, nick.length() - 1);
        if (p.hasPermission("vrn.nick.self")) {
            if (args.length == 0) {
                p.sendMessage(VRNcore.no + "You need to specify a nickname. /nick set, /nick remove, or /nick setplayer.");
            }
            else if (args.length == 1) {
                if (args[0].equalsIgnoreCase("set")) {
                    p.sendMessage(VRNcore.no + "You need to specify a nickname. /nick set <nick>");
                } else if (args[0].equalsIgnoreCase("remove")) {
                    if (plugin.getConfig().getString("nicks." + p.getName()) != null) {
                        this.plugin.getConfig().set("nicks." + p.getName(), null);
                        p.sendMessage(VRNcore.vrn + "Removed your nickname.");
                    } else {
                        p.sendMessage(VRNcore.no + "You don't have a nickname.");
                    }
                } else if (args[0].equalsIgnoreCase("setplayer")) {
                    p.sendMessage(VRNcore.no + "You need to specify a nickname. /nick setplayer <player> <nick>");
                }
                else {
                    p.sendMessage(VRNcore.no + "That is not a choice. /nick set, /nick remove, or /nick setplayer.");
                }
            }
        } else {
            p.sendMessage(VRNcore.noperm);
        }
        if (p.hasPermission("vrn.nick.others")) {
            if (args.length >= 2 && args[0].equalsIgnoreCase("set")) {
                p.sendMessage(VRNcore.color(VRNcore.vrn + "Your nickname is now " + nick + "&r&7."));
                this.plugin.getConfig().set("nicks." + p.getName(), nick);
                this.plugin.saveConfig();
            }
            if (args.length >= 3 && args[0].equalsIgnoreCase("setplayer")) {
                Player a = Bukkit.getPlayerExact(args[1]);
                StringBuilder sb2 = new StringBuilder();
                for (int i = 2; i < args.length; i++) {
                    sb2.append(args[i]).append(" ");
                }
                String nick2 = sb2.toString();

                nick2 = nick2.substring(0, nick2.length() - 1);
                if (a != null) {
                    a.sendMessage(VRNcore.color(VRNcore.vrn + "Your nickname is now " + nick2 + "&r&7."));
                    this.plugin.getConfig().set("nicks." + a.getName(), nick2);
                    this.plugin.saveConfig();
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            }
        } else {
            p.sendMessage(VRNcore.noperm);
        }
        return true;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent e) {
        if (this.plugin.getConfig().getString("nicks." + e.getPlayer().getName()) != null) {
            e.getPlayer().setDisplayName(VRNcore.color(this.plugin.getConfig().getString
                    ("nicks." + e.getPlayer().getName()) + "&r"));
        }
    }
}
