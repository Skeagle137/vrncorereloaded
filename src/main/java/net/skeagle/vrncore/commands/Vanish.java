package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Vanish extends SimpleCommand implements Listener {

    public Vanish() {
        super("vanish");
        setUsage("<player>");
        setDescription("Hide yourself or another player from other players.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1)
            checkConsole();
        final VRNPlayer p = new VRNPlayer(args.length < 1 ? getPlayer() : findPlayer(args[0], VRNUtil.noton));
        checkPerm("vrn.vanish." + (args.length < 1 ? "self" : "others"));
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!p.isVanished())
                pl.hidePlayer(VRNcore.getInstance(), p.getPlayer());
            else
                pl.showPlayer(VRNcore.getInstance(), p.getPlayer());
        }
        p.setVanished(!p.isVanished());
        say(p, "Vanish " + (p.isVanished() ? "enabled." : "disabled."));
        if (args.length < 1 || p.getPlayer() == getSender()) return;
        say(getSender(), "Vanish " + (p.isVanished() ? "enabled" : "disabled") + " for &a" + p.getName() + "&7.");
    }
}


