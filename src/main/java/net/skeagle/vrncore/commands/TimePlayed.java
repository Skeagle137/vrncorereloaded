package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.TimeUtil;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.utils.storage.player.PlayerData;
import net.skeagle.vrncore.utils.storage.player.PlayerManager;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.api.util.TimeUtil.parseTimeString;
import static net.skeagle.vrncore.api.util.TimeUtil.timeToMessage;
import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class TimePlayed extends SimpleCommand {

    public TimePlayed() {
        super("timeplayed|played");
        setUsage("<set|get|add|subtract> [time|player] [player]");
        setDescription("Check or modify a player's time played.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    public void onCommand() {
        checkConsole();
        if (args.length < 1) {
            say(getSender(), "&cPlease use set, get, add, or subtract.");
            return;
        }
        if (args[0].equalsIgnoreCase("set")) {
            long totalsec;
            if (args.length == 1) {
                say(getPlayer(), "&cYou must provide a time.");
                return;
            }
            try {
                totalsec = parseTimeString(args[1]);
            } catch (TimeUtil.TimeFormatException e) {
                say(getPlayer(), e.getMessage());
                return;
            }
            Player p;
            if (args.length > 2) {
                checkPerm("vrn.timeplayed.setothers");
                p = findPlayer(args[2], VRNUtil.noton);
            } else {
                checkConsole();
                checkPerm("vrn.timeplayed.setself");
                p = getPlayer();
            }
            final PlayerData data = PlayerManager.getData(p.getUniqueId());
            data.setTimeplayed(totalsec);
            say(getSender(), "Time played set to &a" + timeToMessage(totalsec) + (isPlayer(p) ? "&7." : "&7 for &a" + p.getName() + "&7."));
        } else if (args[0].equalsIgnoreCase("get")) {
            Player p;
            if (args.length > 1) {
                checkPerm("vrn.timeplayed.getothers");
                p = findPlayer(args[1], VRNUtil.noton);
            } else {
                checkConsole();
                checkPerm("vrn.timeplayed.getself");
                p = getPlayer();
            }
            final PlayerData data = PlayerManager.getData(p.getUniqueId());
            say(getPlayer(), (isPlayer(p) ? "Your" : "&a" + p.getName() + "&7's") + " time played is &a" + timeToMessage(data.getTimeplayed()) + "&7.");
        } else if (args[0].equalsIgnoreCase("add")) {
            long totalsec;
            if (args.length == 1) {
                say(getPlayer(), "&cYou must provide a time.");
                return;
            }
            try {
                totalsec = parseTimeString(args[1]);
            } catch (TimeUtil.TimeFormatException e) {
                say(getPlayer(), e.getMessage());
                return;
            }
            Player p;
            if (args.length > 2) {
                checkPerm("vrn.timeplayed.setothers");
                p = findPlayer(args[2], VRNUtil.noton);
            } else {
                checkConsole();
                checkPerm("vrn.timeplayed.setself");
                p = getPlayer();
            }
            final PlayerData data = PlayerManager.getData(p.getUniqueId());
            final long l = data.getTimeplayed() + totalsec;
            data.setTimeplayed(l);
            say(getSender(), "Added &a" + timeToMessage(totalsec) + "&7 to " + (isPlayer(p) ? "your" : "&a" + p.getName() + "&7's") + " time. " +
                    (isPlayer(p) ? "Your" : "Their") + " total time is now &a" + timeToMessage(l) + "&7.");
        } else if (args[0].equalsIgnoreCase("subtract")) {
            long totalsec;
            if (args.length == 1) {
                say(getPlayer(), "&cYou must provide a time.");
                return;
            }
            try {
                totalsec = parseTimeString(args[1]);
            } catch (TimeUtil.TimeFormatException e) {
                say(getPlayer(), e.getMessage());
                return;
            }
            Player p;
            if (args.length > 2) {
                checkPerm("vrn.timeplayed.setothers");
                p = findPlayer(args[2], VRNUtil.noton);
            } else {
                checkConsole();
                checkPerm("vrn.timeplayed.setself");
                p = getPlayer();
            }
            final PlayerData data = PlayerManager.getData(p.getUniqueId());
            final long l = data.getTimeplayed() - totalsec;
            if (l < 0) {
                data.setTimeplayed(0L);
                say(getPlayer(), "Time played set to &a0 seconds&7.");
                return;
            }
            data.setTimeplayed(l);
            say(getSender(), "Subtracted &a" + timeToMessage(totalsec) + "&7 from " + (isPlayer(p) ? "your" : "&a" + p.getName() + "&7's") + " time. " +
                    (isPlayer(p) ? "Your" : "Their") + " total time is now &a" + timeToMessage(l) + "&7.");
        } else
            say(getSender(), "&cThat is not a valid argument. Please use set, get, add, or subtract.");
    }

    private boolean isPlayer(Player p) {
        return p == getPlayer();
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWord("set", "get", "add", "subtract");
            case 2:
                if (args[0].equalsIgnoreCase("get"))
                    return completeLastWordPlayerNames();
            case 3:
                return completeLastWordPlayerNames();
        }
        return new ArrayList<>();
    }
}
