package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;
import org.mineacademy.fo.settings.YamlConfig;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.timeToMessage;

public class TimePlayed extends SimpleCommand {

    public TimePlayed() {
        super("timeplayed|played");
        setUsage("<set|get> [time|player] [player]");
        setDescription("Check or modify a player's time played.");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            say(getSender(), "&cPlease use set or get.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "set":
                final int time = findNumber(1, "&cYou must include a time.");
                if (args.length > 2) {
                    hasPerm("vrn.timeplayed.setothers");
                    final Player a = findPlayer(args[2], VRNcore.noton);
                    final PlayerCache cache = PlayerCache.getCache(a);
                    cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(time));
                    say(getSender(), "Time played set to &a" + timeToMessage(time) + "&7 for &a" + a.getName() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerCache cache = PlayerCache.getCache(getPlayer());
                cache.setTimeplayed(YamlConfig.TimeHelper.fromSeconds(time));
                say(getPlayer(), "Time played set to &a" + timeToMessage(time) + "&7.");
                return;
            case "get":
                if (args.length > 1) {
                    hasPerm("vrn.timeplayed.getothers");
                    final Player a = findPlayer(args[2], VRNcore.noton);
                    final PlayerCache cache2 = PlayerCache.getCache(a);
                    say(getSender(), "&a" + a.getName() + "&7's time played is &a" + cache2.getTimeplayed() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.getself");
                final PlayerCache cache2 = PlayerCache.getCache(getPlayer());
                say(getPlayer(), "Your time played is &a" + cache2.getTimeplayed() + "&7.");
                return;
            default:
                say(getSender(), "&cThat is not a valid argument. Please use set or get.");
        }
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWord("set", "get");
            case 2:
                if (args[0].equalsIgnoreCase("get")) {
                    return completeLastWordPlayerNames();
                }
            case 3:
                if (args[0].equalsIgnoreCase("set")) {
                    return completeLastWordPlayerNames();
                }
        }
        return new ArrayList<>();
    }
}
