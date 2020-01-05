package net.skeagle.vrncore.commands.weatherAndDay;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;

public class Ptime extends SimpleCommand {
    public Ptime() {
        super("ptime");
        setMinArguments(1);
        setUsage("<day|night|reset>");
        setDescription("Change your personal time.");
        setPermission("vrn.ptime");
        setPermissionMessage(VRNcore.noperm);
    }

    @Override
    protected void onCommand() {
        checkConsole();
        Player p = getPlayer();
        if (args[0].equalsIgnoreCase("day")) {
            p.setPlayerTime(1000, true);
            say(p, "Personal time set to day.");
        } else if (args[0].equalsIgnoreCase("night")) {
            p.setPlayerTime(13000, true);
            say(p, "Personal time set to night.");
        } else if (args[0].equalsIgnoreCase("reset")) {
            p.resetPlayerTime();
            say(p, "Personal time has been reset.");
        } else {
            say(p, "&cThat is not a valid argument.");
        }
    }

    @Override
    protected List<String> tabComplete() {
        if (args.length == 0) {
            return completeLastWord("day", "night", "reset");
        }
        return new ArrayList<>();
    }
}