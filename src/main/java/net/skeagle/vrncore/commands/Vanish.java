package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
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
        if (args.length < 1) {
            checkConsole();
            checkPerm("vrn.vanish.self");
            final VRNPlayer p = new VRNPlayer(getPlayer());
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (!p.isVanished())
                    pl.hidePlayer(VRNcore.getInstance(), p.getPlayer());
                else
                    pl.showPlayer(VRNcore.getInstance(), p.getPlayer());
            }
            p.setVanished(!p.isVanished());
            say(p, "Vanish " + (p.isVanished() ? "enabled." : "disabled."));
            return;
        }
        checkPerm("vrn.vanish.others");
        final VRNPlayer a = new VRNPlayer(findPlayer(args[0], VRNUtil.noton));
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!a.isVanished())
                pl.hidePlayer(VRNcore.getInstance(), a.getPlayer());
            else
                pl.showPlayer(VRNcore.getInstance(), a.getPlayer());
        }
        a.setVanished(!a.isVanished());
        say(a, "Vanish " + (a.isVanished() ? "enabled." : "disabled."));
        say(getSender(), "Vanish " + (a.isVanished() ? "enabled" : "disabled") + " for &a" + a.getName() + "&7.");
    }
}


