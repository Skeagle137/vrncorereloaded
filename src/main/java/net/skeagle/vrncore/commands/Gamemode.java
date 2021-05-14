package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.api.util.VRNUtil;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.ArrayList;
import java.util.List;

import static net.skeagle.vrncore.api.util.VRNUtil.say;

public class Gamemode extends SimpleCommand {

    public Gamemode() {
        super("gamemode|gm");
        setMinArguments(1);
        setUsage("<gamemode> [player]");
        setDescription("Change the gamemode for yourself or another player.");
        setPermission(null);
        setPermissionMessage(VRNUtil.noperm);
    }

    @Override
    protected void onCommand() {
        if (args.length < 2)
            checkConsole();
        final Player p = args.length < 2 ? getPlayer() : findPlayer(args[1], VRNUtil.noton);
        checkPerm("vrn.gamemode." + (args.length < 2 ? "self" : "others"));
        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")) {
            p.setGameMode(GameMode.SURVIVAL);
            sayMessages(p, "survival");
        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")) {
            p.setGameMode(GameMode.CREATIVE);
            sayMessages(p, "creative");
        } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure")) {
            p.setGameMode(GameMode.ADVENTURE);
            sayMessages(p, "adventure");
        } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")) {
            p.setGameMode(GameMode.SPECTATOR);
            sayMessages(p, "spectator");
        } else
            say(p, "&cThat's not a valid game mode. Valid gamemode types are survival, creative, adventure, and spectator.");
    }

    public void sayMessages(final Player p, final String s) {
        say(p, "You are now in " + s + " mode.");
        if (args.length < 2 || p == getSender()) return;
        say(getSender(), "&a" + p.getName() + " &7is now in " + s + " mode.");
    }

    @Override
    protected List<String> tabComplete() {
        switch (args.length) {
            case 1:
                return completeLastWord("survival", "adventure", "creative", "spectator");
            case 2:
                return completeLastWordPlayerNames();
        }
        return new ArrayList<>();
    }
}
