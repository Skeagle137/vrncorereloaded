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

        //to bring back the 1.12 style of changing gamemode
        //I'm too lazy to type the actual words

        if (args.length == 1) {
            checkConsole();
            final Player p = getPlayer();
            checkPerm("vrn.gamemode.self");
            if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")) {
                p.setGameMode(GameMode.SURVIVAL);
                say(p,"You are now in survival mode.");
            } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")) {
                p.setGameMode(GameMode.CREATIVE);
                say(p,"You are now in creative mode.");
            } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure")) {
                p.setGameMode(GameMode.ADVENTURE);
                say(p,"You are now in adventure mode.");
            } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")) {
                p.setGameMode(GameMode.SPECTATOR);
                say(p,"You are now in spectator mode.");
            } else {
                say(p,"&cThat's not a valid game mode. Use /gamemode <gamemode> [player]");
            }
            return;
        }
        final Player a = findPlayer(args[1], VRNUtil.noton);
        checkPerm("vrn.gamemode.others");
        if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("survival") || args[0].equalsIgnoreCase("s")) {
            a.setGameMode(GameMode.SURVIVAL);
            say(a, "You are now in survival mode.");
            say(getSender(), "&a" + a.getName() + " &7is now in survival mode.");
        } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("creative") || args[0].equalsIgnoreCase("c")) {
            a.setGameMode(GameMode.CREATIVE);
            say(a, "You are now in creative mode.");
            say(getSender(), "&a" + a.getName() + " &7is now in creative mode.");
        } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("adventure") || args[0].equalsIgnoreCase("a")) {
            a.setGameMode(GameMode.ADVENTURE);
            say(a, "You are now in adventure mode.");
            say(getSender(), "&a" + a.getName() + " &7is now in adventure mode.");
        } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("spectator") || args[0].equalsIgnoreCase("sp")) {
            a.setGameMode(GameMode.SPECTATOR);
            say(a, "You are now in spectator mode.");
            say(getSender(), "&a" + a.getName() + " &7is now in spectator mode.");
        } else {
            say(getSender(), "&cThat's not a valid game mode. Use /gamemode <gamemode> [player]");
        }
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
