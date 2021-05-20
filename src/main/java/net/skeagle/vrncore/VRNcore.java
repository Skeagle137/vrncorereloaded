package net.skeagle.vrncore;

import net.md_5.bungee.api.ChatColor;
import net.skeagle.vrncore.api.hook.HookManager;
import net.skeagle.vrncore.api.player.VRNPlayer;
import net.skeagle.vrncore.api.sql.SQLConnection;
import net.skeagle.vrncore.api.util.VRNUtil;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.commands.homes.delhome;
import net.skeagle.vrncore.commands.homes.home;
import net.skeagle.vrncore.commands.homes.homes;
import net.skeagle.vrncore.commands.homes.sethome;
import net.skeagle.vrncore.commands.warps.delwarp;
import net.skeagle.vrncore.commands.warps.setwarp;
import net.skeagle.vrncore.commands.warps.warp;
import net.skeagle.vrncore.commands.warps.warps;
import net.skeagle.vrncore.event.*;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.tasks.AutoSaveTask;
import net.skeagle.vrncore.tasks.PlayerTrailTask;
import net.skeagle.vrncore.tasks.PlayerUpdateTask;
import net.skeagle.vrncore.utils.storage.npc.NPCResource;
import net.skeagle.vrncore.utils.storage.timerewards.RewardManager;
import net.skeagle.vrnlib.commandmanager.ArgType;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.CommandParser;
import net.skeagle.vrnlib.commandmanager.Messages;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.util.Collections;
import java.util.List;

import static net.skeagle.vrncore.api.util.VRNUtil.say;
import static net.skeagle.vrncore.api.util.VRNUtil.sayNoPrefix;

public final class VRNcore extends SimplePlugin {

    private PlayerUpdateTask playerTask;
    private PlayerTrailTask trailTask;
    private AutoSaveTask saveTask;

    @Override
    public void onPluginStart() {
        //messages
        Messages.load(this);
        //temp for compatibility, eventually called within api
        HookManager.loadHooks();
        new SQLConnection("");
        //config stuff
        RewardManager.getInstance().loadRewards();
        NPCResource.getInstance().loadAllNPCs();
        //tasks
        playerTask = new PlayerUpdateTask(this);
        trailTask = new PlayerTrailTask(this);
        saveTask = new AutoSaveTask(this);
        //commands
        new CommandParser(this.getResource("commands.txt"))
                .setArgTypes(ArgType.of("entitytype", EntityType.class))
                .parse()
                .register("vrncore", this, new AdminCommands(), new TpCommands(),
                        new TimeWeatherCommands(), new HomesWarpsCommands(), new MiscCommands(), new FunCommands(),
                        new NickCommands(), new NpcCommands());

        registerCommand(new home()); //vrn.home
        registerCommand(new homes()); //vrn.homes
        registerCommand(new delhome()); //vrn.home
        registerCommand(new sethome()); //vrn.sethome
        registerCommand(new warp()); //vrn.warp
        registerCommand(new warps()); //vrn.warps
        registerCommand(new delwarp()); //vrn.delwarp
        registerCommand(new setwarp()); //vrn.setwarp
        //listeners
        registerEvents(new PlayerListener());
        registerEvents(new InvCloseListener());
        registerEvents(new InvClickListener());
        registerEvents(new AFKListener());
        registerEvents(new ArrowListener());
        registerEvents(new ServerListListener());
        registerEvents(new UpdateNPCsListener());
        //enabled
        VRNUtil.log(ChatColor.GREEN +
                        "-------------------------------",
                ChatColor.GREEN + "\t\t  -***************-",
                ChatColor.GREEN + "\t\t  | VRNcore " + this.getDescription().getVersion() + " |",
                ChatColor.GREEN + "\t\t  |***************|",
                ChatColor.GREEN + "\t\t  |   by Skeagle  |",
                ChatColor.GREEN + "\t\t  -***************-",
                ChatColor.GREEN +
                        "-------------------------------");
    }

    public static VRNcore getInstance() {
        return VRNcore.getPlugin(VRNcore.class);
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Collections.singletonList(Settings.class);
    }

    private void cleanBeforeReload() {
        stopTasks(playerTask);
        stopTasks(trailTask);
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
        VRNPlayer p;
        for (final Player pl : Bukkit.getOnlinePlayers()) {
            p = new VRNPlayer(pl);
            p.save();
        }
        //homes.saveAll();
        //warps.saveAll();
    }

    @CommandHook("vrn")
    public void onVRN(final CommandSender sender) {
        sayNoPrefix(sender,
                "&9-----------------------------------------------",
                "&aVRNcore &7is developed and maintained by &dSkeagle&7.",
                "&7This server is currently running version &b" + this.getDescription().getVersion() + "&7.",
                "&7To view the changelog, go to this link:",
                "&bhttps://github.com/Skeagle137/vrncorereloaded/commits",
                "&9-----------------------------------------------");
    }

    @CommandHook("reload")
    public void onReload(final CommandSender sender) {
        Messages.load(this);
        say(sender, "&aConfigs and messages reloaded.");
    }
}

