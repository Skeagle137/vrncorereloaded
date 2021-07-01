package net.skeagle.vrncore;

import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.config.Settings;
import net.skeagle.vrncore.event.AFKListener;
import net.skeagle.vrncore.event.ArrowListener;
import net.skeagle.vrncore.event.MotdListener;
import net.skeagle.vrncore.event.PlayerListener;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.npc.Npc;
import net.skeagle.vrncore.npc.NpcManager;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.rewards.Reward;
import net.skeagle.vrncore.rewards.RewardManager;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import net.skeagle.vrnlib.commandmanager.ArgType;
import net.skeagle.vrnlib.commandmanager.CommandHook;
import net.skeagle.vrnlib.commandmanager.CommandParser;
import net.skeagle.vrnlib.commandmanager.Messages;
import net.skeagle.vrnlib.misc.UserCache;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public final class VRNcore extends JavaPlugin {

    private Settings settings;
    private PlayerManager playerManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private RewardManager rewardManager;
    private NpcManager npcManager;
    private SQLHelper db;

    @Override
    public void onEnable() {
        //messages and config
        Messages.load(this);
        settings = new Settings(this);
        //hooks
        HookManager.loadHooks();
        //database setup
        db = new SQLHelper(SQLHelper.openSQLite(getDataFolder().toPath().resolve("vrn_data.db")));
        db.execute("CREATE TABLE IF NOT EXISTS playerdata (id STRING PRIMARY KEY, nick STRING, arrowtrail STRING, playertrail STRING, " +
                "vanished BOOLEAN, muted BOOLEAN, godmode BOOLEAN, lastOnline BIGINT, lastLocation STRING, timeplayed BIGINT);");
        db.execute("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS npc (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, display STRING, " +
                "location STRING, skin STRING, rotateHead BOOLEAN);");
        db.execute("CREATE TABLE IF NOT EXISTS rewards (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, permission STRING, message STRING, " +
                "firework BOOLEAN, title STRING, subtitle STRING, type STRING, action STRING, groupname STRING, cost BIGINT, time BIGINT);");
        //managers and tasks
        playerManager = new PlayerManager();
        homeManager = new HomeManager();
        warpManager = new WarpManager();
        rewardManager = new RewardManager();
        npcManager = new NpcManager();
        UserCache.asyncInit();
        new Tasks();
        //commands
        new CommandParser(getResource("commands.txt"))
                .setArgTypes(ArgType.of("entitytype", EntityType.class), homeManager.getArgType(),
                        new ArgType<>("warp", warpManager::getWarp).tabStream(s -> warpManager.getWarps().stream().map(Warp::name)),
                        new ArgType<>("reward", rewardManager::getReward).tabStream(s -> rewardManager.getRewards().stream().map(Reward::getName)),
                        new ArgType<>("npc", npcManager::getNpc).tabStream(s -> npcManager.getNpcs().stream().map(Npc::getName)),
                        new ArgType<>("offlineplayer", playerManager::getOfflinePlayer).tabStream(s -> Bukkit.getOnlinePlayers().stream().map(Player::getName)))
                .parse().register("vrncore", this, new AdminCommands(), new TpCommands(),
                new TimeWeatherCommands(), new HomesWarpsCommands(), new MiscCommands(), new FunCommands(),
                new NickCommands(), new NpcCommands(), new RewardCommands());
        //listeners
        if (Settings.Motd.motdEnabled)
            Bukkit.getPluginManager().registerEvents(new MotdListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new AFKListener(), this);
        Bukkit.getPluginManager().registerEvents(new ArrowListener(), this);
    }

    @Override
    public void onDisable() {
        settings.get().save();
        playerManager.save();
        rewardManager.getRewards().forEach(Reward::save);
    }

    public static VRNcore getInstance() {
        return VRNcore.getPlugin(VRNcore.class);
    }

    public SQLHelper getDB() {
        return db;
    }

    public PlayerManager getPlayerManager() {
        return playerManager;
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

    @CommandHook("vrn")
    public void onVRN(final CommandSender sender) {
        sayNoPrefix(sender,
                "&9-----------------------------------------------",
                "&aVRNcore &7is developed and maintained by &dSkeagle&7.",
                "&7This server is currently running version &b" + getDescription().getVersion() + "&7.",
                "&7To view the changelog, go to this link:",
                "&bhttps://github.com/Skeagle137/vrncorereloaded/commits",
                "&9-----------------------------------------------");
    }

    @CommandHook("reload")
    public void onReload(final CommandSender sender) {
        Messages.load(this);
        settings.get().load();
        say(sender, "&aConfigs and messages reloaded.");
    }
}

