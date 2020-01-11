package net.skeagle.vrncore;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.commands.Gamemode;
import net.skeagle.vrncore.commands.homes.delhome;
import net.skeagle.vrncore.commands.homes.home;
import net.skeagle.vrncore.commands.homes.homes;
import net.skeagle.vrncore.commands.homes.sethome;
import net.skeagle.vrncore.commands.nicknames.Nick;
import net.skeagle.vrncore.commands.nicknames.Realname;
import net.skeagle.vrncore.commands.nicknames.RemoveNick;
import net.skeagle.vrncore.commands.tpa.Tpa;
import net.skeagle.vrncore.commands.tpa.Tpaccept;
import net.skeagle.vrncore.commands.tpa.Tpahere;
import net.skeagle.vrncore.commands.tpa.Tpdeny;
import net.skeagle.vrncore.commands.warps.delwarp;
import net.skeagle.vrncore.commands.warps.setwarp;
import net.skeagle.vrncore.commands.warps.warp;
import net.skeagle.vrncore.commands.warps.warps;
import net.skeagle.vrncore.commands.weatherAndDay.*;
import net.skeagle.vrncore.listeners.*;
import net.skeagle.vrncore.utils.NickNameUtil;
import net.skeagle.vrncore.utils.Resources;
import org.bukkit.plugin.PluginDescriptionFile;

import net.md_5.bungee.api.ChatColor;
import org.mineacademy.fo.Common;
import org.mineacademy.fo.plugin.SimplePlugin;

public class VRNcore extends SimplePlugin {

    private PluginDescriptionFile pdf = this.getDescription();
    private String pv = pdf.getVersion();
    public static String noperm = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cYou do not have permission to do this."));
    public static String noton = (ChatColor.translateAlternateColorCodes('&', "&8[&9&lVRN&r&8] &cThat player is not online."));

    private Resources resources;
    private NickNameUtil nickNameUtil;

    public VRNcore() {
        this.resources = new Resources(this);
        this.nickNameUtil = new NickNameUtil(resources);
    }

    @Override
    public void onPluginStart() {
        //config stuff
        resources.load();
        nickNameUtil.loadNicks();
        //server
        Common.log(ChatColor.GREEN + "----------------------------------------",
            ChatColor.GREEN + "VRNcore " + pv + " is now enabled.",
            ChatColor.GREEN + "----------------------------------------");
        //commands
        registerCommand(new Kick()); //vrn.kick
        registerCommand(new TPhere()); //vrn.tphere
        registerCommand(new TPall()); //vrn.tpall
        registerCommand(new GGive()); //vrn.ggive
        registerCommand(new Craft()); //vrn.craft
        registerCommand(new Smite()); //vrn.smite.others
        registerCommand(new Echest()); //vrn.echest.self / vrn.echest.others
        registerCommand(new Invsee()); //vrn.invsee
        registerCommand(new Broadcast()); //vrn.broadcast
        registerCommand(new Mute()); //vrn.mute
        registerCommand(new Godmode()); //vrn.god.self / vrn.god.others
        registerCommand(new Speed()); //vrn.speed
        registerCommand(new Slap()); //vrn.kill.self / vrn.kill.others
        registerCommand(new Sun()); //vrn.weather
        registerCommand(new Rain()); //vrn.weather
        registerCommand(new Day()); //vrn.time
        registerCommand(new Night()); //vrn.time
        registerCommand(new Pweather()); //vrn.pweather
        registerCommand(new Ptime()); //vrn.ptime
        registerCommand(new Rename()); //vrn.rename
        registerCommand(new Vanish()); //vrn.vanish.self / vrn.vanish.others
        registerCommand(new Clearchat()); //vrn.clearchat
        registerCommand(new Gamemode()); //vrn.gamemode.self / vrn.gamemode.others
        registerCommand(new Heal()); //vrn.heal.self / vrn.heal.others
        registerCommand(new Flymode()); //vrn.fly.self / vrn.fly.others
        registerCommand(new Nick(nickNameUtil)); //vrn.nick.self / vrn.nick.others
        registerCommand(new Realname(nickNameUtil)); //vrn.realname
        registerCommand(new RemoveNick(nickNameUtil)); //vrn.nick.self / vrn.nick.others
        registerCommand(new Push()); //vrn.push
        registerCommand(new Back()); //vrn.back
        registerCommand(new home(resources)); //vrn.home
        registerCommand(new homes(resources)); //vrn.homes
        registerCommand(new delhome(resources)); //vrn.home
        registerCommand(new sethome(resources)); //vrn.sethome
        registerCommand(new warp(resources)); //vrn.warp
        registerCommand(new warps(resources)); //vrn.warps
        registerCommand(new delwarp(resources)); //vrn.delwarp
        registerCommand(new setwarp(resources)); //vrn.setwarp
        registerCommand(new Tpa()); //vrn.tpa
        registerCommand(new Tpaccept()); //vrn.tpaccept
        registerCommand(new Tpdeny()); //vrn.tpdeny
        registerCommand(new Tpahere()); //vrn.tpahere
        registerCommand(new Skin()); //vrn.skin
        registerCommand(new Exptrade()); //vrn.exptrade
        //listeners
        registerEvents(new joinEvent());
        registerEvents(new QuitEvent());
        registerEvents(new Vanish());
        registerEvents(new Godmode());
        registerEvents(new Mute());
        registerEvents(new SmallThings());
        registerEvents(new InvCloseListener());
        registerEvents(new InvClickListener());
        registerEvents(new TotalPlayedListener());
        registerEvents(new NickListener(nickNameUtil));
        registerEvents(new BackListener());
    }


    public void onPluginStop() {
        nickNameUtil.saveNicks();
        resources.save();
        Common.log(ChatColor.RED + "----------------------------------------",
                ChatColor.RED + "VRNcore " + pv + " is now disabled.",
                ChatColor.RED + "----------------------------------------");

    }
}

