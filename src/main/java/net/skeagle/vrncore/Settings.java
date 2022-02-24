package net.skeagle.vrncore;

import net.skeagle.vrnlib.config.annotations.Comment;
import net.skeagle.vrnlib.config.annotations.Comments;
import net.skeagle.vrnlib.config.annotations.ConfigName;

public class Settings {

    @Comment("Set to true if the join/leave messages specified in messages be enabled.")
    @ConfigName("join-leave-messages-enabled")
    public static boolean joinLeaveEnabled = true;
    @Comment("The amount of time (in minutes) between saving playerdata.")
    @ConfigName("auto-save-time-minutes")
    public static int autoSaveInterval = 15;

    //motd
    @Comment("Set to true to let VRNCore handle the motd.")
    @ConfigName("motd-enabled")
    public static boolean motdEnabled = false;
    @Comment("Set to true if the text specified in first-line-text should be shown.")
    @ConfigName("first-line-shown")
    public static boolean firstLineShown = true;
    @Comment("The text for the first line of the motd. Will not show if first-line-shown is false.")
    @ConfigName("first-line-text")
    public static String firstLineText = "My Server";
    @Comments({@Comment("The text for the second line of the motd. If first-line-shown is false,"),
            @Comment("then this text will be the only text shown for the motd."),
            @Comment("This text will have no effect if random-motd is set to true.")})
    @ConfigName("second-line-text")
    public static String secondLineText = "This is a second line";
    @Comment("If true, the text in second-line-text will be replaced with a random line from motds.txt.")
    @ConfigName("random-motd")
    public static boolean randomMotd = true;

    //afk
    @Comment("The amount of time, in seconds, before the server marks a player as afk and stops counting their playtime.")
    @ConfigName("marked-afk-seconds")
    public static int afktime = 60;
    @Comment("The amount of time, in seconds, before a player is kicked for being afk too long.")
    @ConfigName("afk-kick-seconds")
    public static int kickTime = 1800;

    //chat
    @Comment("Set to true to let VRNCore handle the chat.")
    @ConfigName("chat-enabled")
    public static boolean chatEnabled = true;
    @Comment("Set to true if players should need the permission \"vrn.chat.allow\" to talk in chat.")
    @ConfigName("chat-allow-permission")
    public static boolean chatPermission = false;
    @Comment("Set to true if players should need the permission \"vrn.chat.color\" to use color and style codes in chat.")
    @ConfigName("chat-color-permission")
    public static boolean colorPermission = true;
    @Comment("The format that all chat messages will show as.")
    @ConfigName("chat-format")
    public static String format = "%prefix%player%suffix: %message";
    @Comment("The format that all player names in the tab list will show as.")
    @ConfigName("list-format")
    public static String listFormat = "%prefix %player %suffix";
    @Comment("If true, players may have multiple prefixes in chat.")
    @ConfigName("allow-multiple-prefixes")
    public static boolean multiplePrefix = true;
    @Comment("If true, players may have multiple suffixes in chat.")
    @ConfigName("allow-multiple-suffixes")
    public static boolean multipleSuffix = true;

    //rtp
    @Comment("The minimum amount of blocks that /rtp will teleport players from the origin.")
    @ConfigName("rtp-min")
    public static int rtpMin = 100;
    @Comment("The maximum amount of blocks that /rtp will teleport players from the origin.")
    @ConfigName("rtp-max")
    public static int rtpMax = 10000;
    @Comment("The X offset that rtp will apply to the origin before teleporting (0 is the spawn X coordinate).")
    @ConfigName("rtp-originX")
    public static int rtpOriginX = 0;
    @Comment("The Z offset that rtp will apply to the origin before teleporting (0 is the spawn Z coordinate).")
    @ConfigName("rtp-originZ")
    public static int rtpOriginZ = 0;

    //homes & warps
    @Comment("The hard limit for the max amount of warps a player can have.")
    @ConfigName("max-amount-warps")
    public static int maxWarps = 100;
    @Comment("The hard limit for the max amount of homes a player can have.")
    @ConfigName("max-amount-homes")
    public static int maxHomes = 100;
    @Comment("Set to true to enable a more efficient, but potentially more performance inducing, method for checking limit permissions.")
    @ConfigName("alternate-limit-permission-check")
    public static boolean alternatePermLimitCheck = false;
}