package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.mineacademy.fo.command.SimpleCommand;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Vanish extends SimpleCommand implements Listener {

    public Vanish() {
        super("vanish");
        setUsage("<player>");
        setDescription("Hide yourself or another player from other players.");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            checkConsole();
            final Player p = getPlayer();
            hasPerm("vrn.vanish.self");
            final PlayerData data = PlayerManager.getData(p);
            for (final Player pl : Bukkit.getOnlinePlayers()) {
                if (!data.getVanished())
                    pl.hidePlayer(VRNcore.getInstance(), p);
                else
                    pl.showPlayer(VRNcore.getInstance(), p);
            }
            data.setVanished(!data.getVanished());
            say(p, "Vanish " + (data.getVanished() ? "enabled." : "disabled."));
            return;
        }
        hasPerm("vrn.vanish.others");
        final Player a = findPlayer(args[0], VRNUtil.noton);
        final PlayerData data = PlayerManager.getData(a);
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            if (!data.getVanished())
                pl.hidePlayer(VRNcore.getInstance(), a);
            else
                pl.showPlayer(VRNcore.getInstance(), a);
        }
        data.setVanished(!data.getVanished());
        say(a, "Vanish " + (data.getVanished() ? "enabled." : "disabled."));
        say(getSender(), "Vanish " + (data.getVanished() ? "enabled" : "disabled") + " for &a" + a.getName() + "&7.");
    }
}


