package net.skeagle.vrncore;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class CommandVRN implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String cp = "6";
        if (args.length == 0) {
            sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 1 of " + cp + "]&r&a-----"));
            sender.sendMessage(VRNcore.color("&2/vrn &a- displays this help page."));
            sender.sendMessage(VRNcore.color("&2/vrnversion &a- shows the current plugin version."));
            sender.sendMessage(VRNcore.color("&2/clearchat &a- clears the chat."));
            sender.sendMessage(VRNcore.color("&2/sysinfo &a- shows server platform information."));
            sender.sendMessage(VRNcore.color("&2/gamemode &a- changes a player's gamemode."));
            sender.sendMessage(VRNcore.color("&4/clear &c- Disabled."));
            sender.sendMessage(VRNcore.color("&2/heal &a- heals you."));
            sender.sendMessage(VRNcore.color("&2/setspawn &a- sets the server spawnpoint."));
            sender.sendMessage(VRNcore.color("&aDo &2/vrn 2&a for the next page."));
            sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 1 of " + cp +"]&r&a-----"));
        }
        if (args.length >= 1) {
            if (args[0].equalsIgnoreCase("1")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 1 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/vrn &a- displays this help page."));
                sender.sendMessage(VRNcore.color("&2/vrnversion &a- shows the current plugin version."));
                sender.sendMessage(VRNcore.color("&2/clearchat &a- clears the chat."));
                sender.sendMessage(VRNcore.color("&2/sysinfo &a- shows server platform information."));
                sender.sendMessage(VRNcore.color("&2/gamemode &a- changes a player's gamemode."));
                sender.sendMessage(VRNcore.color("&4/clear &c- Disabled."));
                sender.sendMessage(VRNcore.color("&2/heal &a- heals you."));
                sender.sendMessage(VRNcore.color("&2/setspawn &a- sets the server spawnpoint."));
                sender.sendMessage(VRNcore.color("&aDo &2/vrn 2&a for the next page."));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 1 of " + cp + "]&r&a-----"));
            }
            else if (args[0].equalsIgnoreCase("2")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 2 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/spawn &a- teleports you to the server spawnpoint."));
                sender.sendMessage(VRNcore.color("&2/fly &a- toggles flight mode."));
                sender.sendMessage(VRNcore.color("&2/vanish &a- toggles player visibility."));
                sender.sendMessage(VRNcore.color("&2/rename &a- rename an item. &cCurrently buggy."));
                sender.sendMessage(VRNcore.color("&2/pweather &a- toggle personal weather."));
                sender.sendMessage(VRNcore.color("&2/ptime &a- toggle personal time."));
                sender.sendMessage(VRNcore.color("&2/day &a- change the time to day."));
                sender.sendMessage(VRNcore.color("&2/night &a- change the time to night."));
                sender.sendMessage(VRNcore.color("&aDo &2/vrn 3&a for the next page."));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 2 of " + cp + "]&r&a-----"));
            }
            else if (args[0].equalsIgnoreCase("3")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 3 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/sun &a- change the weather to sunny."));
                sender.sendMessage(VRNcore.color("&2/rain &a- change the weather to rainy."));
                sender.sendMessage(VRNcore.color("&2/god &a- Toggles invulnerability."));
                sender.sendMessage(VRNcore.color("&2/broadcast &a- Broadcasts a message to chat."));
                sender.sendMessage(VRNcore.color("&2/echest &a- Opens a player's ender chest."));
                sender.sendMessage(VRNcore.color("&2/slap &a- Slap a player."));
                sender.sendMessage(VRNcore.color("&2/speed &a- Modify your fly or walking speed."));
                sender.sendMessage(VRNcore.color("&2/mute &a- Toggles a player's ability to chat."));
                sender.sendMessage(VRNcore.color("&aDo &2/vrn 4&a for the next page."));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 3 of " + cp + "]&r&a-----"));
            }
            else if (args[0].equalsIgnoreCase("4")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 4 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/invsee &a- Opens a player's inventory."));
                sender.sendMessage(VRNcore.color("&2/smite &a- Smite a player."));
                sender.sendMessage(VRNcore.color("&2/craft &a- Open a virtual crafting table."));
                sender.sendMessage(VRNcore.color("&2/enchant &a- Open a virtual enchantment table."));
                sender.sendMessage(VRNcore.color("&2/ggive &a- A GUI /give manager."));
                sender.sendMessage(VRNcore.color("&2/trails &a- Player trails. &bWIP."));
                sender.sendMessage(VRNcore.color("&2/tphere &a- Teleport another player to your location."));
                sender.sendMessage(VRNcore.color("&2/tpall &a- Teleport all players to your location."));
                sender.sendMessage(VRNcore.color("&aDo &2/vrn 5&a for the next page."));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 4 of " + cp + "]&r&a-----"));
            }
            else if (args[0].equalsIgnoreCase("5")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 5 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/setwarp &a- set a warp point at your current location."));
                sender.sendMessage(VRNcore.color("&2/warp &a- Teleport to a warp point."));
                sender.sendMessage(VRNcore.color("&2/warps &a- List all current warp points. &cCurrently buggy."));
                sender.sendMessage(VRNcore.color("&2/delwarp &a- Delete a warp point."));
                sender.sendMessage(VRNcore.color("&2/ban &a- Bans a player with a reason. &bWIP."));
                sender.sendMessage(VRNcore.color("&2/kick &a- Kicks a player with a reason."));
                sender.sendMessage(VRNcore.color("&2/sethome &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/home &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 5 of " + cp + "]&r&a-----"));
            }
            else if (args[0].equalsIgnoreCase("6")) {
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 6 of " + cp + "]&r&a-----"));
                sender.sendMessage(VRNcore.color("&2/delhome &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/stats &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/vrnreload &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/skull &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/wings &e- Coming soon!"));
                sender.sendMessage(VRNcore.color("&2/nick &d- Possibly?"));
                sender.sendMessage(VRNcore.color("&a-----&a&l[VRNcore Help Page 6 of " + cp + "]&r&a-----"));
            }
            else {
                sender.sendMessage(VRNcore.no + "That page does not exist.");
            }
        }
        return true;
    }
}

