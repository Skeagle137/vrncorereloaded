package net.skeagle.vrncore;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

import net.md_5.bungee.api.ChatColor;

public class VRNcore extends JavaPlugin implements CommandExecutor {

    private VRNcore plugin;
    private PluginDescriptionFile pdf = this.getDescription();
    private String pv = pdf.getVersion();
    static String vrn = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &7"));
    static String vrn2 = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &a"));
    static String joinquit = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &d"));
    static String no = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &c"));
    static String welcome = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &e"));
    static String sp = (ChatColor.translateAlternateColorCodes('&', no + "You must specify a player."));
    static String tma = (ChatColor.translateAlternateColorCodes('&', no + "Too many args!"));
    static String noperm = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cYou don't have permission."));
    static String noton = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cThat player is not online."));
    static String broadcast = (ChatColor.translateAlternateColorCodes('&', "&8[&3&lVRN Broadcast&r&8] &b"));

    @Override
    public void onEnable() {
        plugin = this;
        //config stuff
        this.getConfig().options().copyDefaults(true);
        this.saveConfig();
        //server
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "VRNcore version " + pv + " is now enabled.");
        getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "----------------------------------------");
        //commands
        this.getCommand("nick").setExecutor(new Nick(this));         //vrn.nick
        this.getCommand("kick").setExecutor(new Kick());                     //vrn.kick
        this.getCommand("ban").setExecutor(new Ban());                       //vrn.ban
        this.getCommand("tphere").setExecutor(new TPhere());                 //vrn.tphere
        this.getCommand("tpall").setExecutor(new TPall());                   //vrn.tpall
        this.getCommand("ggive").setExecutor(new openGUIs(this));    //vrn.ggive
        this.getCommand("enchant").setExecutor(new VirtualStuff());          //vrn.enchant
        this.getCommand("craft").setExecutor(new VirtualStuff());            //vrn.craft
        this.getCommand("smite").setExecutor(new Smite());                   //vrn.smite.self / vrn.smite.others
        this.getCommand("echest").setExecutor(new Invsee());                 //vrn.echest.self / vrn.echest.others
        this.getCommand("invsee").setExecutor(new Invsee());                 //vrn.invsee.self / vrn.invsee.others
        this.getCommand("broadcast").setExecutor(new Broadcast());           //vrn.broadcast
        this.getCommand("mute").setExecutor(new Mute());                     //vrn.mute
        this.getCommand("god").setExecutor(new Godmode());                   //vrn.god.self / vrn.god.others
        this.getCommand("speed").setExecutor(new Speed());                   //vrn.speed
        this.getCommand("slap").setExecutor(new Kill());                     //vrn.slap.self / vrn.slap.others
        this.getCommand("rain").setExecutor(new Dayandweatherchange());      //vrn.weather
        this.getCommand("sun").setExecutor(new Dayandweatherchange());       //vrn.weather
        this.getCommand("night").setExecutor(new Dayandweatherchange());     //vrn.time
        this.getCommand("day").setExecutor(new Dayandweatherchange());       //vrn.time
        this.getCommand("pweather").setExecutor(new Dayandweatherchange());  //vrn.pweather
        this.getCommand("ptime").setExecutor(new Dayandweatherchange());     //vrn.ptime
        this.getCommand("rename").setExecutor(new Rename());                 //vrn.rename
        this.getCommand("vanish").setExecutor(new Vanish(this));     //vrn.vanish.self / vrn.vanish.others
        this.getCommand("clearchat").setExecutor(new CommandClearChat());    //vrn.clearchat
        this.getCommand("vrn").setExecutor(new CommandVRN());
        this.getCommand("sysinfo").setExecutor(new CommandSysInfo());        //vrn.sysinfo
        this.getCommand("gamemode").setExecutor(new GamemodeC());            //vrn.gamemode.self / vrn.gamemode.others
        //this.getCommand("clear").setExecutor(new ClearInventory());  disabled, conflicts with normal /clear
        this.getCommand("heal").setExecutor(new Heal());                     //vrn.heal.self / vrn.heal.others
        this.getCommand("spawn").setExecutor(new Setspawn());                //vrn.spawn
        this.getCommand("setspawn").setExecutor(new Setspawn());             //vrn.setspawn
        this.getCommand("fly").setExecutor(new Flymode());                   //vrn.fly.self / vrn.fly.others
        this.getCommand("vrnversion").setExecutor(this);
        //listeners
        Bukkit.getPluginManager().registerEvents(new joinEvent(), this);
        Bukkit.getPluginManager().registerEvents(new QuitEvent(), this);
        Bukkit.getPluginManager().registerEvents(new Vanish(this), this);
        Bukkit.getPluginManager().registerEvents(new Godmode(), this);
        Bukkit.getPluginManager().registerEvents(new Mute(), this);
        Bukkit.getPluginManager().registerEvents(new GUIgive(), this);
        Bukkit.getPluginManager().registerEvents(new SmallThings(), this);
        Bukkit.getPluginManager().registerEvents(new Nick(this), this);
    }

    @Override
    public void onDisable() {
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "-----------------------------------------");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "VRNcore version " + pv + " is now disabled.");
        getServer().getConsoleSender().sendMessage(ChatColor.RED + "-----------------------------------------");

    }

    public boolean onCommand(CommandSender sender, Command cmd, String commandLabel, String[] args) {
        if (commandLabel.equalsIgnoreCase("vrnversion")) {
            Player p = (Player)sender;
            p.sendMessage(ChatColor.translateAlternateColorCodes('&',vrn + "The current version of VRNcore is &a" + pv + "&7."));
        }
        return true;
    }

    static String color(String i) {
        return ChatColor.translateAlternateColorCodes('&', i);
    }
}

