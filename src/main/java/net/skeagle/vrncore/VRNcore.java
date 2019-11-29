package net.skeagle.vrncore;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.commands.weatherAndDay.*;
import net.skeagle.vrncore.listeners.GUIgive;
import net.skeagle.vrncore.listeners.QuitEvent;
import net.skeagle.vrncore.listeners.SmallThings;
import net.skeagle.vrncore.listeners.joinEvent;
import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginDescriptionFile;

import net.md_5.bungee.api.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public class VRNcore extends SimplePlugin {

    private VRNcore plugin;
    private PluginDescriptionFile pdf = this.getDescription();
    private String pv = pdf.getVersion();
    public static String vrn = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &7"));
    public static String vrn2 = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &a"));
    public static String joinquit = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &d"));
    public static String no = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &c"));
    public static String welcome = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &e"));
    public static String sp = (ChatColor.translateAlternateColorCodes('&', no + "You must specify a player."));
    public static String tma = (ChatColor.translateAlternateColorCodes('&', no + "Too many args!"));
    public static String noperm = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cYou don't have permission."));
    public static String noton = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cThat player is not online."));
    public static String broadcast = (ChatColor.translateAlternateColorCodes('&', "&8[&3&lVRN Broadcast&r&8] &b"));

    @Override
    public void onPluginStart() {
        plugin = this;
        //config stuff

        //this.getConfig().options().copyDefaults(true);
        //this.saveConfig();

        //server
        Common.log(ChatColor.GREEN + "----------------------------------------",
            ChatColor.GREEN + "VRNcore version " + pv + " is now enabled.",
            ChatColor.GREEN + "----------------------------------------");
        //commands
        registerCommand(new Kick()); //vrn.kick
        registerCommand(new TPhere()); //vrn.tphere
        registerCommand(new TPall()); //vrn.tpall
        registerCommand(new openGGive()); //vrn.ggive
        registerCommand(new Craft()); //vrn.craft
        registerCommand(new Smite()); //vrn.smite.self / vrn.smite.others
        registerCommand(new Echest()); //vrn.echest.self / vrn.echest.others
        registerCommand(new Invsee()); //vrn.invsee.self / vrn.invsee.others
        registerCommand(new Broadcast()); //vrn.broadcast
        registerCommand(new Mute()); //vrn.mute
        registerCommand(new Godmode()); //vrn.god.self / vrn.god.others
        registerCommand(new Speed()); //vrn.speed
        registerCommand(new Kill()); //vrn.kill.self / vrn.kill.others
        registerCommand(new Sun()); //vrn.weather
        registerCommand(new Rain()); //vrn.weather
        registerCommand(new Day()); //vrn.time
        registerCommand(new Night()); //vrn.time
        registerCommand(new Pweather()); //vrn.pweather
        registerCommand(new Ptime()); //vrn.ptime
        registerCommand(new Rename()); //vrn.rename
        registerCommand(new Vanish(this)); //vrn.vanish.self / vrn.vanish.others
        registerCommand(new Clearchat()); //vrn.clearchat
        registerCommand(new Vrn());
        registerCommand(new Gamemode()); //vrn.gamemode.self / vrn.gamemode.others
        registerCommand(new Heal()); //vrn.heal.self / vrn.heal.others
        registerCommand(new Flymode()); //vrn.fly.self / vrn.fly.others
        //listeners
        Bukkit.getPluginManager().registerEvents(new joinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Vanish(this), this);
        Bukkit.getPluginManager().registerEvents(new Godmode(), this);
        Bukkit.getPluginManager().registerEvents(new Mute(), this);
        Bukkit.getPluginManager().registerEvents(new GUIgive(), this);
        Bukkit.getPluginManager().registerEvents(new SmallThings(), this);
    }


    public void onPluginStop() {
        Common.log(ChatColor.RED + "----------------------------------------",
                ChatColor.RED + "VRNcore version " + pv + " is now disabled.",
                ChatColor.RED + "----------------------------------------");

    }

    public static String color(String i) {
        return ChatColor.translateAlternateColorCodes('&', i);
    }
}

