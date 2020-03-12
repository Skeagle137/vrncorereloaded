package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.Bukkit;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.color;

public class Broadcast extends SimpleCommand {

    public Broadcast() {
        super("broadcast|bc");
        setMinArguments(1);
        setUsage("<message>");
        setDescription("Broadcast a message to the entire server.");
        setPermission("vrn.broadcast");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        final StringBuilder sb = new StringBuilder();
        for (final String arg : args) {
            sb.append(arg).append(" ");
        }
        Bukkit.broadcastMessage(color("&8[&3&lVRN Broadcast&r&8] &b" + sb.toString()));
    }
}
