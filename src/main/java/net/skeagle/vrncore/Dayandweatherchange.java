package net.skeagle.vrncore;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import static org.bukkit.WeatherType.CLEAR;
import static org.bukkit.WeatherType.DOWNFALL;

public class Dayandweatherchange implements CommandExecutor {
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player p = (Player)sender;
        if (label.equalsIgnoreCase("ptime")) {
            if (p.hasPermission("vrn.ptime")) {
                if (args.length == 0) {
                    p.sendMessage(VRNcore.no + "You need to specify 'day' or 'night.'");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("day")) {
                        p.setPlayerTime(1000, true);
                        p.sendMessage(VRNcore.vrn + "Personal time set to day.");
                    } else if (args[0].equalsIgnoreCase("night")) {
                        p.setPlayerTime(13000, true);
                        p.sendMessage(VRNcore.vrn + "Personal time set to night.");
                    } else if (args[0].equalsIgnoreCase("reset")) {
                        p.resetPlayerTime();
                        p.sendMessage(VRNcore.vrn + "Personal time has been reset.");
                    } else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument.");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("pweather")) {
            if (p.hasPermission("vrn.pweather")) {
                if (args.length == 0) {
                    p.sendMessage(VRNcore.no + "You need to specify 'sun,' or 'rain.'");
                } else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("sun")) {
                        p.setPlayerWeather(CLEAR);
                        p.sendMessage(VRNcore.vrn + "Personal weather set to sun.");
                    } else if (args[0].equalsIgnoreCase("rain")) {
                        p.setPlayerWeather(DOWNFALL);
                        p.sendMessage(VRNcore.vrn + "Personal weather set to rain.");
                    } else if (args[0].equalsIgnoreCase("reset")) {
                        p.resetPlayerWeather();
                        p.sendMessage(VRNcore.vrn + "Personal weather has been reset.");
                    } else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument.");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("sun")) {
            if (p.hasPermission("vrn.weather")) {
                if (args.length == 0) {
                    p.getLocation().getWorld().setStorm(false);
                    p.sendMessage(VRNcore.vrn + "Weather set to sun.");
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for(World w : Bukkit.getServer().getWorlds()) {
                            w.setStorm(false);
                        }
                        p.sendMessage(VRNcore.vrn + "Weather set to sun in all worlds.");
                    }
                    else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/sun all.'");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("rain")) {
            if (p.hasPermission("vrn.weather")) {
                if (args.length == 0) {
                    p.getLocation().getWorld().setStorm(true);
                    p.sendMessage(VRNcore.vrn + "Weather set to rain.");
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for(World w : Bukkit.getServer().getWorlds()) {
                            w.setStorm(true);
                        }
                        p.sendMessage(VRNcore.vrn + "Weather set to rain in all worlds.");
                    }
                    else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/rain all.'");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("day")) {
            if (p.hasPermission("vrn.time")) {
                if (args.length == 0) {
                    p.getLocation().getWorld().setTime(1000);
                    p.sendMessage(VRNcore.vrn + "Time set to day.");
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for(World w : Bukkit.getServer().getWorlds()) {
                            w.setTime(1000);
                        }
                        p.sendMessage(VRNcore.vrn + "Time set to day in all worlds.");
                    }
                    else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/day all.'");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        if (label.equalsIgnoreCase("night")) {
            if (p.hasPermission("vrn.time")) {
                if (args.length == 0) {
                    p.getLocation().getWorld().setTime(13000);
                    p.sendMessage(VRNcore.vrn + "Time set to night.");
                }
                else if (args.length == 1) {
                    if (args[0].equalsIgnoreCase("all")) {
                        for(World w : Bukkit.getServer().getWorlds()) {
                            w.setTime(13000);
                        }
                        p.sendMessage(VRNcore.vrn + "Time set to night in all worlds.");
                    }
                    else {
                        p.sendMessage(VRNcore.no + "That is not a valid argument. Do '/night all.'");
                    }
                }
            }
            else {
                p.sendMessage(VRNcore.noperm);
            }
        }
        return true;
    }
}
