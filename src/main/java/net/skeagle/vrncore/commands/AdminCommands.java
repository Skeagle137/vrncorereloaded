package net.skeagle.vrncore.commands;

import net.skeagle.vrnlib.commandmanager.CommandHook;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class AdminCommands {

    @CommandHook("sudo")
    public void onSudo(final CommandSender sender, final Player target, final String command) {
        if (!command.startsWith("/")) {
            target.chat(command);
            return;
        }
        Bukkit.dispatchCommand(target, command.substring(1));
        say(sender, "Made target execute command: &a" + command);
    }
}
