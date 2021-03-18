package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Bukkit;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.color;

public class Clearchat extends SimpleCommand {

    public Clearchat() {
        super("clearchat");
        setDescription("Clears the chat.");
        setPermission("vrn.clearchat");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        for (int i = 0; i < 150; i++) {
            Bukkit.broadcastMessage("");
        }
        Bukkit.broadcastMessage(color("&a" + getSender().getName() + " &7has cleared the chat."));
    }
}
