package net.skeagle.vrncore.settings;

import org.mineacademy.fo.settings.SimpleSettings;

import java.util.List;

public class Settings extends SimpleSettings {

    @Override
    protected int getConfigVersion() {
        return 1;
    }

    public static class Motd {
        public static Boolean ENABLED;
        public static Boolean FIRST_STATIC;
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
        public static Integer STOP_COUNTING;
        public static Integer SECONDS_DELAY;
        public static Integer KICK_TIME_IN_SECONDS;

        private static void init() {
            pathPrefix("Afk");

            SECONDS_DELAY = getInteger("Seconds_Delay");
            STOP_COUNTING = getInteger("Stop_Counting_Playtime");
            KICK_TIME_IN_SECONDS = getInteger("Kick_Time_In_Seconds");
        }
    }

    public static String PREFIX;

    private static void init() {
        pathPrefix(null);

        PREFIX = getString("Prefix");
    }
}