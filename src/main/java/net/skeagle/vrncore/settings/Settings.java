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

    private static void init() {
        pathPrefix(null);
    }
}