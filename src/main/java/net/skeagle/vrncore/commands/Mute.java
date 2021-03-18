package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

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
        final VRNPlayer a = new VRNPlayer(findPlayer(args[0], VRNUtil.noton));
        a.setMuted(!a.isMuted());
        say(a, "You are " + (a.isMuted() ? "now" : "no longer") + " muted.");
        say(getSender(), "&a" + a.getName() + " &7is " + (a.isMuted() ? "now" : "no longer") + " muted.");
    }
}
