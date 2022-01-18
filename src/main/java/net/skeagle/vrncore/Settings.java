package net.skeagle.vrncore;

public class Settings {

    public static boolean joinLeaveEnabled = true;
    public static int autoSaveInterval = 15;

    //motd
    public static boolean motdEnabled = false;
    public static boolean firstLineShown = true;
    public static String firstLineText = "My Server";
    public static boolean randomMotd = true;
    public static String secondLineText = "This is a second line";

    //afk
    public static int afktime = 60;
    public static int kickTime = 1800;

    //chat
    public static boolean chatEnabled = true;
    public static boolean chatPermission = false;
    public static boolean colorPermission = true;
    public static String format = "%prefix%player%suffix: %message";
    public static String listFormat = "%prefix %player %suffix";
    public static boolean multiplePrefix = true;
    public static boolean multipleSuffix = true;

    //rtp
    public static int rtpMin = 100;
    public static int rtpMax = 10000;
    public static int rtpOriginX = 0;
    public static int rtpOriginZ = 0;
}