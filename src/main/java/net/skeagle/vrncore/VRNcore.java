package net.skeagle.vrncore;

import net.md_5.bungee.api.ChatColor;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.event.*;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.npc.Npc;
import net.skeagle.vrncore.npc.NpcManager;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.settings.Settings;
import net.skeagle.vrncore.timerewards.RewardManager;
import net.skeagle.vrncore.utils.TaskSetup;
import net.skeagle.vrncore.utils.VRNUtil;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import net.skeagle.vrnlib.commandmanager.ArgType;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.CommandParser;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.mineacademy.fo.plugin.SimplePlugin;
import org.mineacademy.fo.settings.YamlStaticConfig;

import java.sql.Connection;
import java.util.Collections;
import java.util.List;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public final class VRNcore extends SimplePlugin {

    private HomeManager homeManager;
    private WarpManager warpManager;
    private RewardManager rewardManager;
    private NpcManager npcManager;
    private Connection conn;

    @Override
    public void onPluginStart() {
        //messages and config
        Messages.load(this);
        RewardManager.getInstance().loadRewards();
        //hooks
        HookManager.loadHooks();
        //database setup
        conn = SQLHelper.openSQLite(this.getDataFolder().toPath().resolve("vrn_data.db"));
        final SQLHelper sql = new SQLHelper(conn);
        sql.execute("CREATE TABLE IF NOT EXISTS playerdata (id STRING PRIMARY KEY, nick STRING, arrowtrail STRING, playertrail STRING, " +
                "vanished BOOLEAN, muted BOOLEAN, godmode BOOLEAN, lastOnline BIGINT, lastLocation STRING, timeplayed BIGINT);");
        sql.execute("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        sql.execute("CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        sql.execute("CREATE TABLE IF NOT EXISTS npc (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, display STRING, " +
                "location STRING, skin STRING, rotateHead BOOLEAN);");
        sql.execute("CREATE TABLE IF NOT EXISTS rewards (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, type STRING, timeRequired BIGINT, cost BIGINT);");
        //managers and tasks
        homeManager = new HomeManager();
        warpManager = new WarpManager();
        rewardManager = new RewardManager();
        npcManager = new NpcManager();
        new TaskSetup();
        //commands
        new CommandParser(this.getResource("commands.txt"))
                .setArgTypes(ArgType.of("entitytype", EntityType.class), homeManager.getArgType(),
                        new ArgType<>("warp", warpManager::getWarp).tabStream(s -> warpManager.getWarps().stream().map(Warp::getName)),
                        new ArgType<>("npc", npcManager::getNpc).tabStream(s -> npcManager.getNpcs().stream().map(Npc::getName)))
                .parse().register("vrncore", this, new AdminCommands(), new TpCommands(),
                new TimeWeatherCommands(), new HomesWarpsCommands(), new MiscCommands(), new FunCommands(),
                new NickCommands(), new NpcCommands());
        //listeners
        registerEvents(new PlayerListener());
        registerEvents(new InvCloseListener());
        registerEvents(new InvClickListener());
        registerEvents(new AFKListener());
        registerEvents(new ArrowListener());
        registerEvents(new ServerListListener());
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

    public SQLHelper getDB() {
        return new SQLHelper(conn);
    }

    public HomeManager getHomeManager() {
        return homeManager;
    }

    public WarpManager getWarpManager() {
        return warpManager;
    }

    public RewardManager getRewardManager() {
        return rewardManager;
    }

    public NpcManager getNpcManager() {
        return npcManager;
    }

    @Override
    public List<Class<? extends YamlStaticConfig>> getSettings() {
        return Collections.singletonList(Settings.class);
    }

    @Override
    public void onPluginStop() {
        PlayerManager.save();
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

