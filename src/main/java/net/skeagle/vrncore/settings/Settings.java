package net.skeagle.vrncore.settings;

import org.mineacademy.fo.settings.SimpleSettings;

import java.util.List;

public class Settings extends SimpleSettings {

    @Override
    protected int getConfigVersion() {
        return 3;
    }

    public static class Motd {
        public static Boolean ENABLED, FIRST_STATIC;
        public static String FIRST_STATIC_TEXT;
        public static List<String> MESSAGES;

        private static void init() {
            pathPrefix("Motd");

            ENABLED = getBoolean("Enabled");
            FIRST_STATIC = getBoolean("First_Line_Static");
            FIRST_STATIC_TEXT = getString("First_Line_Text");
            MESSAGES = getStringList("Messages");
        }
    }

    public static class Afk {
        public static Integer STOP_COUNTING, KICK_TIME_IN_SECONDS;

        private static void init() {
            pathPrefix("Afk");

            STOP_COUNTING = getInteger("Stop_Counting_Playtime");
            KICK_TIME_IN_SECONDS = getInteger("Kick_Time_In_Seconds");
        }
    }

    public static class Chat {
        public static Boolean ENABLED, ALL_MAY_COLOR, MULTIPLE_PREFIX, MULTIPLE_SUFFIX;
        public static String FORMAT;

        private static void init() {
            pathPrefix("Chat");

            ENABLED = getBoolean("Chat_Enabled");
            ALL_MAY_COLOR = getBoolean("No_Permissions_Chat_Color");
            FORMAT = getString("Message_Format");
            MULTIPLE_PREFIX = getBoolean("Multiple_Prefixes");
            MULTIPLE_SUFFIX = getBoolean("Multiple_Suffixes");
        }
    }

    public static class Rtp {
        public static Integer min_x, max_x, min_z, max_z;
        public static Double x, z;

        private static void init() {
            pathPrefix("Rtp");
            min_x = getInteger("min_distance_x");
            max_x = getInteger("max_distance_x");
            min_z = getInteger("min_distance_z");
            max_z = getInteger("max_distance_z");
            x = getDouble("x_origin");
            z = getDouble("z_origin");
        }
    }

    public static class Joining {
        public static Boolean ENABLED;
        public static String WELCOME, JOIN, QUIT;

        private static void init() {
            pathPrefix("Joining");
            ENABLED = getBoolean("custom_join_leave_enabled");
            WELCOME = getString("welcome_message");
            JOIN = getString("join_message");
            QUIT = getString("leave_message");
        }
    }

    /*
    public static class Holograms {
        public static Integer MAX_LINES;
        public static Integer MAX_LINE_LENGTH;

        private static void init() {
            pathPrefix("Holograms");

            MAX_LINES = getInteger("Max_Lines");
            MAX_LINE_LENGTH = getInteger("Max_Line_Length");
        }

    }

     */

    public static String PREFIX;

    private static void init() {
        pathPrefix(null);

        PREFIX = getString("Prefix");
    }
}