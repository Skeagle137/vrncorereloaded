package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.PlayerCache;
import net.skeagle.vrncore.utils.TimeFormatException;
import net.skeagle.vrncore.utils.TimeUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

import static net.skeagle.vrncore.utils.TimeUtil.fromSeconds;
import static net.skeagle.vrncore.utils.TimeUtil.timeToMessage;
import static net.skeagle.vrncore.utils.VRNUtil.say;

public class TimePlayed extends SimpleCommand {

    public TimePlayed() {
        super("timeplayed|played");
        setUsage("<set|get|add|subtract> [time|player] [player]");
        setDescription("Check or modify a player's time played.");
        setPermission("vrn.timeplayed");
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        if (args.length < 1) {
            say(getSender(), "&cPlease use set, get, add, or subtract.");
            return;
        }
        switch (args[0].toLowerCase()) {
            case "set":
                int index = 1;
                long totalsec = 0;
                try {
                    for (int i = 1; i < args.length; i++) {
                        final boolean b = TimeUtil.TimeString.isTimeString(args, i);
                        if (b) {
                            final TimeUtil.TimeString ts;
                            ts = TimeUtil.TimeString.toTimeString(args[i]);
                            totalsec += TimeUtil.TimeString.toSeconds(ts);
                            index++;
                        }
                    }
                } catch (final TimeFormatException ex) {
                    return;
                }
                if (index == 1) {
                    if (args.length > 1) {
                        say(getPlayer(), "&cInvalid format, please check your command and try again.");
                        return;
                    }
                    say(getPlayer(), "&cPlease provide a valid time format.");
                    return;
                }
                if (args.length > (index)) {
                    hasPerm("vrn.timeplayed.setothers");
                    final Player a = findPlayer(args[index], VRNUtil.noton);
                    final PlayerCache cache = PlayerCache.getCache(a);
                    cache.setTimeplayed(fromSeconds(totalsec));
                    say(getSender(), "Time played set to &a" + timeToMessage(totalsec) + "&7 for &a" + a.getName() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerCache cache = PlayerCache.getCache(getPlayer());
                cache.setTimeplayed(fromSeconds(totalsec));
                say(getPlayer(), "Time played set to &a" + timeToMessage(totalsec) + "&7.");
                return;
            case "get":
                if (args.length > 1) {
                    hasPerm("vrn.timeplayed.getothers");
                    final Player a = findPlayer(args[1], VRNUtil.noton);
                    final PlayerCache cache2 = PlayerCache.getCache(a);
                    say(getSender(), "&a" + a.getName() + "&7's time played is &a" + timeToMessage(cache2.getTimeplayed().getTimeSeconds()) + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.getself");
                final PlayerCache cache2 = PlayerCache.getCache(getPlayer());
                say(getPlayer(), "Your time played is &a" + timeToMessage(cache2.getTimeplayed().getTimeSeconds()) + "&7.");
                return;
            case "add":
                int index1 = 1;
                long totalsec1 = 0;
                try {
                    for (int i = 1; i < args.length; i++) {
                        final boolean b = TimeUtil.TimeString.isTimeString(args, i);
                        if (b) {
                            final TimeUtil.TimeString ts;
                            ts = TimeUtil.TimeString.toTimeString(args[i]);
                            totalsec1 += TimeUtil.TimeString.toSeconds(ts);
                            index1++;
                        }
                    }
                } catch (final TimeFormatException ex) {
                    return;
                }
                if (index1 == 1) {
                    if (args.length > 1) {
                        say(getPlayer(), "&cInvalid format, please check your command and try again.");
                        return;
                    }
                    say(getPlayer(), "&cPlease provide a valid time format.");
                    return;
                }
                if (args.length > (index1)) {
                    hasPerm("vrn.timeplayed.setothers");
                    final Player a = findPlayer(args[index1], VRNUtil.noton);
                    final PlayerCache cache3 = PlayerCache.getCache(a);
                    final long l = cache3.getTimeplayed().getTimeSeconds();
                    cache3.setTimeplayed(fromSeconds(l + totalsec1));
                    say(getSender(), "Time played set to &a" + timeToMessage(totalsec1) + "&7 for &a" + a.getName() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerCache cache4 = PlayerCache.getCache(getPlayer());
                final long l = cache4.getTimeplayed().getTimeSeconds();
                cache4.setTimeplayed(fromSeconds(l + totalsec1));
                say(getPlayer(), "Time played set to &a" + timeToMessage(totalsec1) + "&7.");
                return;
            case "subtract":
                int index2 = 1;
                long totalsec2 = 0;
                try {
                    for (int i = 1; i < args.length; i++) {
                        final boolean b = TimeUtil.TimeString.isTimeString(args, i);
                        if (b) {
                            final TimeUtil.TimeString ts;
                            ts = TimeUtil.TimeString.toTimeString(args[i]);
                            totalsec2 += TimeUtil.TimeString.toSeconds(ts);
                            index2++;
                        }
                    }
                } catch (final TimeFormatException ex) {
                    return;
                }
                if (index2 == 1) {
                    if (args.length > 1) {
                        say(getPlayer(), "&cInvalid format, please check your command and try again.");
                        return;
                    }
                    say(getPlayer(), "&cPlease provide a valid time format.");
                    return;
                }
                if (args.length > (index2)) {
                    hasPerm("vrn.timeplayed.setothers");
                    final Player a = findPlayer(args[index2], VRNUtil.noton);
                    final PlayerCache cache5 = PlayerCache.getCache(a);
                    final long l2 = cache5.getTimeplayed().getTimeSeconds();
                    if (l2 - totalsec2 < 0) {
                        cache5.setTimeplayed(fromSeconds(0));
                        say(getPlayer(), "Time played set to &a0 seconds&7.");
                    } else {
                        cache5.setTimeplayed(fromSeconds(l2 - totalsec2));
                        say(getPlayer(), "Time played set to &a" + timeToMessage(l2 - totalsec2) + "&7.");
                    }
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerCache cache6 = PlayerCache.getCache(getPlayer());
                final long l2 = cache6.getTimeplayed().getTimeSeconds();
                if (l2 - totalsec2 < 0) {
                    cache6.setTimeplayed(fromSeconds(0));
                    say(getPlayer(), "Time played set to &a0 seconds&7.");
                } else {
                    cache6.setTimeplayed(fromSeconds(l2 - totalsec2));
                    say(getPlayer(), "Time played set to &a" + timeToMessage(l2 - totalsec2) + "&7.");
                }
                return;
            default:
                say(getSender(), "&cThat is not a valid argument. Please use set, get, add, or subtract.");
        }
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWord("set", "get", "add", "subtract");
            case 2:
                if (args[0].equalsIgnoreCase("get")) {
                    return completeLastWordPlayerNames();
                }
            case 3:
                if (args[0].equalsIgnoreCase("set") ||
                        args[0].equalsIgnoreCase("add") ||
                        args[0].equalsIgnoreCase("subtract")) {
                    return completeLastWordPlayerNames();
                }
        }
        return completeLastWordPlayerNames();
    }
}
