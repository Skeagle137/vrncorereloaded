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

    private PluginDescriptionFile pdf = this.getDescription();
    private String pv = pdf.getVersion();
    public static String vrn = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &7"));
    public static String no = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &c"));
    public static String noperm = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cYou don't have permission."));
    public static String noton = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cThat player is not online."));

    @Override
    public void onPluginStart() {
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
        Common.registerEvents(new joinEvent());
        Common.registerEvents(new QuitEvent());
        Common.registerEvents(new Vanish(this));
        Common.registerEvents(new Godmode());
        Common.registerEvents(new Mute());
        Common.registerEvents(new GUIgive());
        Common.registerEvents(new SmallThings());
    }


    public void onPluginStop() {
        Common.log(ChatColor.RED + "----------------------------------------",
                ChatColor.RED + "VRNcore v" + pv + " is now disabled.",
                ChatColor.RED + "----------------------------------------");

    }

    public static String color(String i) {
        return ChatColor.translateAlternateColorCodes('&', i);
    }
}

