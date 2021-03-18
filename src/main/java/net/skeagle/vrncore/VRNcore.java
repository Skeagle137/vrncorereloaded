package net.skeagle.vrncore;

import net.md_5.bungee.api.ChatColor;
import net.skeagle.vrncore.api.hook.HookManager;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.commands.homes.*;
import net.skeagle.vrncore.commands.nicknames.Nick;
import net.skeagle.vrncore.commands.nicknames.Realname;
import net.skeagle.vrncore.commands.nicknames.RemoveNick;
import net.skeagle.vrncore.commands.tpa.Tpa;
import net.skeagle.vrncore.commands.tpa.Tpaccept;
import net.skeagle.vrncore.commands.tpa.Tpahere;
import net.skeagle.vrncore.commands.tpa.Tpdeny;
import net.skeagle.vrncore.commands.warps.*;
import net.skeagle.vrncore.commands.weatherAndDay.*;
import net.skeagle.vrncore.api.sql.SQLConnection;
import net.skeagle.vrncore.event.*;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.tasks.*;
import net.skeagle.vrncore.utils.storage.npc.NPCResource;
import net.skeagle.vrncore.utils.storage.timerewards.RewardManager;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Collections;
import java.util.List;

public final class VRNcore extends SimplePlugin {

    private UpdateAFKPlayerTask afkTask;
    private PlayerTrailTask trailTask;
    private PlayerSitTask sitTask;
    private AutoSaveTask saveTask;

    @Override
    public void onPluginStart() {
        //temp for compatibility, eventually called within api
        HookManager.loadHooks();
        new SQLConnection("");
        //config stuff
        RewardManager.getInstance().loadRewards();
        NPCResource.getInstance().loadAllNPCs();
        //server
        VRNUtil.log(ChatColor.GREEN +
                "-------------------------------\n" +
                ChatColor.GREEN + "\t\t  -***************-\n" +
                ChatColor.GREEN + "\t\t  | VRNcore " + getVersion() + " |\n" +
                ChatColor.GREEN + "\t\t  |***************|\n" +
                ChatColor.GREEN + "\t\t  |   by Skeagle  |\n" +
                ChatColor.GREEN + "\t\t  -***************-\n" +
                ChatColor.GREEN +
                "-------------------------------");
        //tasks
        afkTask = new UpdateAFKPlayerTask(this);
        trailTask = new PlayerTrailTask(this);
        sitTask = new PlayerSitTask(this);
        saveTask = new AutoSaveTask(this);
        //commands
        registerCommand(new Kick()); //vrn.kick
        registerCommand(new TPhere()); //vrn.tphere
        registerCommand(new TPall()); //vrn.tpall
        registerCommand(new GGive()); //vrn.ggive
        registerCommand(new Craft()); //vrn.craft
        registerCommand(new Smite()); //vrn.smite.others
        registerCommand(new Echest()); //vrn.echest.self|others
        registerCommand(new Invsee()); //vrn.invsee
        registerCommand(new Broadcast()); //vrn.broadcast
        registerCommand(new Mute()); //vrn.mute
        registerCommand(new Godmode()); //vrn.god.self|others
        registerCommand(new Speed()); //vrn.speed
        registerCommand(new Slap()); //vrn.slap.self|others
        registerCommand(new Sun()); //vrn.weather
        registerCommand(new Rain()); //vrn.weather
        registerCommand(new Thunder()); //vrn.weather
        registerCommand(new Day()); //vrn.time
        registerCommand(new Night()); //vrn.time
        registerCommand(new Pweather()); //vrn.pweather
        registerCommand(new Ptime()); //vrn.ptime
        registerCommand(new Rename()); //vrn.rename
        registerCommand(new Vanish()); //vrn.vanish.self|others
        registerCommand(new Clearchat()); //vrn.clearchat
        registerCommand(new Gamemode()); //vrn.gamemode.self|others
        registerCommand(new Heal()); //vrn.heal.self|others
        registerCommand(new Fly()); //vrn.fly.self|others
        registerCommand(new Nick()); //vrn.nick.self|others
        registerCommand(new Realname()); //vrn.realname
        registerCommand(new RemoveNick()); //vrn.nick.self|others
        registerCommand(new Push()); //vrn.push
        registerCommand(new Back()); //vrn.back
        registerCommand(new home()); //vrn.home
        registerCommand(new homes()); //vrn.homes
        registerCommand(new delhome()); //vrn.home
        registerCommand(new sethome()); //vrn.sethome
        registerCommand(new warp()); //vrn.warp
        registerCommand(new warps()); //vrn.warps
        registerCommand(new delwarp()); //vrn.delwarp
        registerCommand(new setwarp()); //vrn.setwarp
        registerCommand(new Tpa()); //vrn.tpa
        registerCommand(new Tpaccept()); //vrn.tpaccept
        registerCommand(new Tpdeny()); //vrn.tpdeny
        registerCommand(new Tpahere()); //vrn.tpahere
        registerCommand(new Skin()); //vrn.skin
        registerCommand(new Exptrade()); //vrn.exptrade
        registerCommand(new Trails()); //vrn.trails
        registerCommand(new Demo()); //vrn.demo
        registerCommand(new Spawnmob()); //vrn.spawnmob
        registerCommand(new TimePlayed()); //vrn.timeplayed.setself|getself|setothers|getothers
        registerCommand(new Hallucinate()); //vrn.hallucinate.self|others
        registerCommand(new Npc()); //vrn.npc
        registerCommand(new Top()); //vrn.top
        registerCommand(new Rtp()); //vrn.rtp
        registerCommand(new Vrn()); //vrn.reload
        registerCommand(new Sit()); //vrn.sit
        //listeners
        registerEvents(new PlayerListener());
        registerEvents(new InvCloseListener());
        registerEvents(new InvClickListener());
        registerEvents(new AFKListener());
        registerEvents(new BackListener());
        registerEvents(new ArrowListener());
        registerEvents(new ServerListListener());
        registerEvents(new UpdateNPCsListener());
        registerEvents(new PlayerSitListener());

    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Collections.singletonList(Settings.class);
    }

    @Override
    protected void onPluginReload() {
        cleanBeforeReload();
    }

    private void cleanBeforeReload() {
        stopTasks(afkTask);
        stopTasks(trailTask);
        stopTasks(sitTask);
        stopTasks(saveTask);
    }

    private void stopTasks(final BukkitRunnable task) {
        if (task != null) {
            try {
                task.cancel();
            } catch (final IllegalStateException ignored) {
            }
        }
    }

    @Override
    public void onPluginStop() {
        cleanBeforeReload();
    }
}

