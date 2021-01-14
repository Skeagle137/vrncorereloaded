package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Mute extends SimpleCommand implements Listener {

    public Mute() {
        super("mute");
        setMinArguments(1);
        setUsage("<player>");
        setDescription("Mute a player.");
        setPermission("vrn.mute");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final PlayerData data = PlayerManager.getData(a);
        data.setMuted(!data.getMuted());
        say(a, "You are " + (data.getMuted() ? "now" : "no longer") + " muted.");
        say(getSender(), "&a" + a.getName() + " &7is " + (data.getMuted() ? "now" : "no longer") + " muted.");
    }
}
