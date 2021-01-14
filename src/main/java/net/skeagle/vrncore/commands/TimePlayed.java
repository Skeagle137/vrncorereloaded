package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.utils.TimeFormatException;
import net.skeagle.vrncore.utils.TimeUtil;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.List;

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
                int totalsec = 0;
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
                    final PlayerData data = PlayerManager.getData(a);
                    data.setTimeplayed((long) totalsec);
                    say(getSender(), "Time played set to &a" + timeToMessage(totalsec) + "&7 for &a" + a.getName() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerData data = PlayerManager.getData(getPlayer());
                data.setTimeplayed((long) totalsec);
                say(getPlayer(), "Time played set to &a" + timeToMessage(totalsec) + "&7.");
                return;
            case "get":
                if (args.length > 1) {
                    hasPerm("vrn.timeplayed.getothers");
                    final Player a = findPlayer(args[1], VRNUtil.noton);
                    final PlayerData data2 = PlayerManager.getData(a);
                    say(getSender(), "&a" + a.getName() + "&7's time played is &a" + timeToMessage(data2.getTimeplayed()) + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.getself");
                final PlayerData data2 = PlayerManager.getData(getPlayer());
                say(getPlayer(), "Your time played is &a" + timeToMessage(data2.getTimeplayed()) + "&7.");
                return;
            case "add":
                int index1 = 1;
                int totalsec1 = 0;
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
                    final PlayerData data3 = PlayerManager.getData(a);
                    final Long l = data3.getTimeplayed();
                    data3.setTimeplayed(l + totalsec1);
                    say(getSender(), "Time played set to &a" + timeToMessage(totalsec1) + "&7 for &a" + a.getName() + "&7.");
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerData data4 = PlayerManager.getData(getPlayer());
                final Long l = data4.getTimeplayed();
                data4.setTimeplayed(l + totalsec1);
                say(getPlayer(), "Time played set to &a" + timeToMessage(totalsec1) + "&7.");
                return;
            case "subtract":
                int index2 = 1;
                int totalsec2 = 0;
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
                    final PlayerData data5 = PlayerManager.getData(a);
                    final Long l2 = data5.getTimeplayed();
                    if (l2 - totalsec2 < 0) {
                        data5.setTimeplayed(0L);
                        say(getPlayer(), "Time played set to &a0 seconds&7.");
                    } else {
                        data5.setTimeplayed(l2 - totalsec2);
                        say(getPlayer(), "Time played set to &a" + timeToMessage(l2 - totalsec2) + "&7.");
                    }
                    return;
                }
                checkConsole();
                hasPerm("vrn.timeplayed.setself");
                final PlayerData data6 = PlayerManager.getData(getPlayer());
                final Long l2 = data6.getTimeplayed();
                if (l2 - totalsec2 < 0) {
                    data6.setTimeplayed(0L);
                    say(getPlayer(), "Time played set to &a0 seconds&7.");
                } else {
                    data6.setTimeplayed(l2 - totalsec2);
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
