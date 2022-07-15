package net.skeagle.vrncore;

import net.luckperms.api.event.node.NodeMutateEvent;
import net.luckperms.api.model.user.User;
import net.skeagle.vrncommands.*;
import net.skeagle.vrncore.commands.*;
import net.skeagle.vrncore.event.AFKListener;
import net.skeagle.vrncore.event.TrailHandler;
import net.skeagle.vrncore.event.MotdListener;
import net.skeagle.vrncore.event.PlayerListener;
import net.skeagle.vrncore.homes.HomeManager;
import net.skeagle.vrncore.hook.HookManager;
import net.skeagle.vrncore.hook.LuckPermsHook;
import net.skeagle.vrncore.npc.Npc;
import net.skeagle.vrncore.npc.NpcManager;
import net.skeagle.vrncore.playerdata.PlayerData;
import net.skeagle.vrncore.playerdata.PlayerManager;
import net.skeagle.vrncore.rewards.RewardManager;
import net.skeagle.vrncore.trail.style.StyleRegistry;
import net.skeagle.vrncore.warps.Warp;
import net.skeagle.vrncore.warps.WarpManager;
import net.skeagle.vrnlib.config.ConfigManager;
import net.skeagle.vrnlib.misc.Task;
import net.skeagle.vrnlib.misc.UserCache;
import net.skeagle.vrnlib.sql.SQLHelper;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.server.ServerListPingEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.sqlite.Function;
import org.sqlite.SQLiteException;

import java.sql.SQLException;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.logging.Level;
import java.util.regex.Pattern;

import static net.skeagle.vrncore.utils.VRNUtil.say;
import static net.skeagle.vrncore.utils.VRNUtil.sayNoPrefix;

public final class VRNcore extends JavaPlugin {

    private ConfigManager config;
    private PlayerManager playerManager;
    private HomeManager homeManager;
    private WarpManager warpManager;
    private RewardManager rewardManager;
    private NpcManager npcManager;
    private StyleRegistry styleRegistry;
    private SQLHelper db;
    private MotdListener motdListener;

    @Override
    @SuppressWarnings("deprecation")
    public void onEnable() {
        //messages and config
        config = ConfigManager.create(this).target(Settings.class).saveDefaults().load();
        BukkitMessages.load(this);
        //hooks
        HookManager.loadHooks();
        //database setup
        db = new SQLHelper(SQLHelper.openSQLite(getDataFolder().toPath().resolve("vrn_data.db")));
        db.execute("CREATE TABLE IF NOT EXISTS playerdata (id STRING PRIMARY KEY, nick STRING, playerTrailData STRING, arrowTrailData STRING, playerStates STRING, timePlayed BIGINT);");
        db.execute("CREATE TABLE IF NOT EXISTS homes (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS warps (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, owner STRING, location STRING);");
        db.execute("CREATE TABLE IF NOT EXISTS npc (id INTEGER PRIMARY KEY AUTOINCREMENT, name STRING, display STRING, " +
                "location STRING, skin STRING, rotateHead BOOLEAN);");
        //managers and tasks
        UserCache.asyncInit();
        playerManager = new PlayerManager(this);
        styleRegistry = new StyleRegistry(this);
        homeManager = new HomeManager(db);
        warpManager = new WarpManager(db);
        rewardManager = new RewardManager(this);
        npcManager = new NpcManager();
        new Tasks(this);
        //commands
        new BukkitCommandParser(getResource("commands.txt"))
                .setContextProviders(ContextProvider.self, new ContextProvider<>("selfoffline", s -> CompletableFuture.completedFuture(s.getUser())))
                .setArgTypes(ArgType.of("entitytype", EntityType.class), homeManager.getArgType(),
                        new ArgType<>("warp", warpManager::getWarp).tabStream(s -> warpManager.getWarps().stream().map(Warp::name)),
                        new ArgType<>("npc", npcManager::getNpc).tabStream(s -> npcManager.getNpcs().stream().map(Npc::getName)),
                        new ArgType<>("offlineplayer", playerManager::getOfflinePlayer).tabStream(s -> Bukkit.getOnlinePlayers().stream().map(Player::getName)))
                .parse().register(new BukkitCommandRegistry(this), "vrncore", this, new AdminCommands(), new TpCommands(),
                        new TimeWeatherCommands(), new HomesWarpsCommands(this), new MiscCommands(), new FunCommands(),
                        new NickCommands(this), new NpcCommands());
        //listeners
        this.reloadMotds();
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);
        Bukkit.getPluginManager().registerEvents(new AFKListener(), this);
        Bukkit.getPluginManager().registerEvents(new TrailHandler(this), this);
        if (HookManager.isLuckPermsLoaded()) {
            new LuckPermsHook().getLuckperms().getEventBus().subscribe(this, NodeMutateEvent.class, e -> {
                if (!e.isUser()) return;
                Player player = Bukkit.getPlayer(((User) e.getTarget()).getUniqueId());
                if (player == null) return;
                playerManager.getData(player.getUniqueId()).thenAccept(data -> Task.syncDelayed(data::updateName));
            });
        }
    }

    @Override
    public void onDisable() {
        config.save();
        playerManager.save();
        rewardManager.save();
    }

    private void reloadMotds() {
        if (Settings.motdEnabled && motdListener == null) {
            motdListener = new MotdListener(this);
            Bukkit.getPluginManager().registerEvents(motdListener, this);
        }
        else if (!Settings.motdEnabled && motdListener != null) {
            motdListener = null;
            ServerListPingEvent.getHandlerList().unregister(this);
        }
        if (motdListener != null) {
            motdListener.loadMotds();
        }
    }

    public static VRNcore getInstance() {
        return VRNcore.getPlugin(VRNcore.class);
    }

    public static CompletableFuture<PlayerData> getPlayerData(UUID uuid) {
        return VRNcore.getInstance().getPlayerManager().getData(uuid);
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
        BukkitMessages.load(this);
        config.reload();
        rewardManager.reload();
        this.reloadMotds();
        say(sender, "&aConfigs, messages, and rewards reloaded.");
    }
}

