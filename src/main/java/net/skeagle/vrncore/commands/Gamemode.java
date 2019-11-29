package net.skeagle.vrncore.commands;

import net.skeagle.vrncore.VRNcore;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.mineacademy.fo.command.SimpleCommand;

import java.util.Collections;

public class Gamemode extends SimpleCommand {

    public Gamemode() {
        super("gamemode", Collections.singletonList("gm"));
    }

    @Override
    protected void onCommand() {

        //to bring back the 1.12 style of changing gamemode
        //I'm too lazy to type the actual words

        if (sender instanceof ConsoleCommandSender) {
            sender.sendMessage(VRNcore.vrn + "You must be a Player!");
        }
        if (args.length == 0) {
            sender.sendMessage(VRNcore.vrn + "You must specify a gamemode. /gamemode [gamemode] <player>");
        } else if (args.length == 1) {
            Player p = (Player) sender;

            if (p.hasPermission("vrn.gamemode.self")) {

                if (args[0].equalsIgnoreCase("0") || args[0].equalsIgnoreCase("s") || args[0].equalsIgnoreCase("survival")) {
                    p.setGameMode(GameMode.SURVIVAL);
                    p.sendMessage(VRNcore.vrn + "You are now in survival mode.");
                } else if (args[0].equalsIgnoreCase("1") || args[0].equalsIgnoreCase("c") || args[0].equalsIgnoreCase("creative")) {
                    p.setGameMode(GameMode.CREATIVE);
                    p.sendMessage(VRNcore.vrn + "You are now in creative mode.");
                } else if (args[0].equalsIgnoreCase("2") || args[0].equalsIgnoreCase("a") || args[0].equalsIgnoreCase("adventure")) {
                    p.setGameMode(GameMode.ADVENTURE);
                    p.sendMessage(VRNcore.vrn + "You are now in adventure mode.");
                } else if (args[0].equalsIgnoreCase("3") || args[0].equalsIgnoreCase("sp") || args[0].equalsIgnoreCase("spectator")) {
                p.setGameMode(GameMode.SPECTATOR);
                p.sendMessage(VRNcore.vrn + "You are now in spectator mode.");

                } else {
                    p.sendMessage(VRNcore.no + "Syntax error, use /gamemode [gamemode] <player>");
                }

            } else {
                p.sendMessage(VRNcore.noperm);
            }
        } else if (args.length == 2) {
            Player p = (Player) sender;
            Player a = Bukkit.getPlayer(args[1]);

            if (p.hasPermission("vrn.gamemode.others")) {
                if (a != null) {

                    //change gamemode with numbers
                    if (args[0].equalsIgnoreCase("0")) {
                        a.setGameMode(GameMode.SURVIVAL);
                        a.sendMessage(VRNcore.vrn + "You are now in survival mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in survival mode!"));
                    } else if (args[0].equalsIgnoreCase("1")) {
                        a.setGameMode(GameMode.CREATIVE);
                        a.sendMessage(VRNcore.vrn + "You are now in creative mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in creative mode!"));
                    } else if (args[0].equalsIgnoreCase("2")) {
                        a.setGameMode(GameMode.ADVENTURE);
                        a.sendMessage(VRNcore.vrn + "You are now in adventure mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in adventure mode!"));
                    } else if (args[0].equalsIgnoreCase("3")) {
                        a.setGameMode(GameMode.SPECTATOR);
                        a.sendMessage(VRNcore.vrn + "You are now in spectator mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in spectator mode!"));

                        //actual words
                    } else if (args[0].equalsIgnoreCase("survival")) {
                        a.setGameMode(GameMode.SURVIVAL);
                        a.sendMessage(VRNcore.vrn + "You are now in survival mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in survival mode!"));
                    } else if (args[0].equalsIgnoreCase("creative")) {
                        a.setGameMode(GameMode.CREATIVE);
                        a.sendMessage(VRNcore.vrn + "You are now in creative mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in creative mode!"));
                    } else if (args[0].equalsIgnoreCase("adventure")) {
                        a.setGameMode(GameMode.ADVENTURE);
                        a.sendMessage(VRNcore.vrn + "You are now in adventure mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in adventure mode!"));
                    } else if (args[0].equalsIgnoreCase("spectator")) {
                        a.setGameMode(GameMode.SPECTATOR);
                        a.sendMessage(VRNcore.vrn + "You are now in spectator mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in spectator mode!"));

                        //one letter style
                    } else if (args[0].equalsIgnoreCase("s")) {
                        a.setGameMode(GameMode.SURVIVAL);
                        a.sendMessage(VRNcore.vrn + "You are now in survival mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in survival mode!"));
                    } else if (args[0].equalsIgnoreCase("c")) {
                        a.setGameMode(GameMode.CREATIVE);
                        a.sendMessage(VRNcore.vrn + "You are now in creative mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in creative mode!"));
                    } else if (args[0].equalsIgnoreCase("a")) {
                        a.setGameMode(GameMode.ADVENTURE);
                        a.sendMessage(VRNcore.vrn + "You are now in adventure mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() +  " &7is now in adventure mode!"));
                    } else if (args[0].equalsIgnoreCase("sp")) {
                        a.setGameMode(GameMode.SPECTATOR);
                        a.sendMessage(VRNcore.vrn + "You are now in spectator mode.");
                        p.sendMessage(ChatColor.translateAlternateColorCodes('&', VRNcore.vrn2 + a.getName() + " &7is now in spectator mode!"));
                    } else {
                        p.sendMessage(VRNcore.vrn + "That's not a game mode.");
                    }
                } else {
                    p.sendMessage(VRNcore.noton);
                }
            } else {
                p.sendMessage(VRNcore.noperm);
            }
        }
    }
}
