package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class Clearchat extends SimpleCommand {

    public Clearchat() {
        super("clearchat");
        setDescription("Clears the chat.");
        setPermission("vrn.clearchat");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        for (int i = 0; i < 150; i++) {
            Bukkit.broadcastMessage("");
        }
        Bukkit.broadcastMessage(color("&a" + getSender().getName() + " &7has cleared the chat."));
    }
}
