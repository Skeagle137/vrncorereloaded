package net.skeagle.vrncore;

import net.skeagle.vrncommands.*;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.event.AFKListener;
import net.skeagle.vrncore.event.TrailListener;
import net.skeagle.vrncore.event.MotdListener;
import net.skeagle.vrncore.event.PlayerListener;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.npc.Npc;
import net.skeagle.vrncore.npc.NpcManager;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.rewards.RewardManager;
import net.skeagle.vrncore.trail.style.StyleRegistry;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import net.skeagle.vrnlib.config.ConfigManager;
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

    private BukkitCommandRegistry registry;
    private ConfigManager config;
    private PlayerManager playerManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private RewardManager rewardManager;
    private NpcManager npcManager;
    private StyleRegistry styleRegistry;
    private SQLHelper db;

    @Override
    public void onEnable() {
        registry = new BukkitCommandRegistry(this);
        //messages and config
        config = ConfigManager.create(this).target(Settings.class).saveDefaults().load();
        BukkitMessages.load(registry);
        //hooks
        HookManager.loadHooks();
        //database setup
        db = new SQLHelper(SQLHelper.openSQLite(getDataFolder().toPath().resolve("vrn_data.db")));
        db.execute("CREATE TABLE IF NOT EXISTS playerdata (id STRING PRIMARY KEY, nick STRING, arrowtrail STRING, playertrail STRING, " +
                "trailStyle STRING, playerStates STRING, timePlayed BIGINT);");
        db.execute("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS npc (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, display STRING, " +
                "location STRING, skin STRING, rotateHead BOOLEAN);");
        //managers and tasks
        UserCache.asyncInit();
        playerManager = new PlayerManager();
        styleRegistry = new StyleRegistry(this);
        homeManager = new HomeManager();
        warpManager = new WarpManager();
        rewardManager = new RewardManager(this);
        npcManager = new NpcManager();
        new Tasks();
        //commands
        new BukkitCommandParser(getResource("commands.txt"))
                .setArgTypes(ArgType.of("entitytype", EntityType.class), homeManager.getArgType(),
                        new ArgType<>("warp", warpManager::getWarp).tabStream(s -> warpManager.getWarps().stream().map(Warp::name)),
                        new ArgType<>("npc", npcManager::getNpc).tabStream(s -> npcManager.getNpcs().stream().map(Npc::getName)),
                        new ArgType<>("offlineplayer", playerManager::getOfflinePlayer).tabStream(s -> Bukkit.getOnlinePlayers().stream().map(Player::getName)))
                .parse().register(registry, "vrncore", this, new AdminCommands(), new TpCommands(),
                new TimeWeatherCommands(), new HomesWarpsCommands(), new MiscCommands(), new FunCommands(),
                new NickCommands(), new NpcCommands());
        //listeners
        if (Settings.motdEnabled)
            Bukkit.getPluginManager().registerEvents(new MotdListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(), this);
        Bukkit.getPluginManager().registerEvents(new AFKListener(), this);
        Bukkit.getPluginManager().registerEvents(new TrailListener(), this);
    }

    @Override
    public void onDisable() {
        config.save();
        playerManager.save();
        rewardManager.save();
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

    public StyleRegistry getStyleRegistry() {
        return styleRegistry;
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
        BukkitMessages.load(registry);
        config.reload();
        rewardManager.reload();
        say(sender, "&aConfigs, messages, and rewards reloaded.");
    }
}

