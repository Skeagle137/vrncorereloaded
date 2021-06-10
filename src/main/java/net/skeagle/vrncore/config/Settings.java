package net.skeagle.vrncore.config;

import net.skeagle.vrncore.VRNcore;
import net.skeagle.vrnlib.configmanager.ConfigManager;
import net.skeagle.vrnlib.configmanager.annotations.ConfigValue;

public class Settings {

    @ConfigValue("join-leave-messages-enabled")
    public static boolean joinLeaveEnabled = true;

    private ConfigManager config;

    public Settings(VRNcore plugin) {
        config = new ConfigManager(plugin).register(this, new Motd(), new Afk(), new Chat(), new Rtp()).saveDefaults().load();
    }

    private Settings() {
    }

    public static class Motd {
        @ConfigValue("motd-enabled")
        public static boolean motdEnabled = false;
        @ConfigValue("first-line-shown")
        public static boolean firstLineShown = true;
        @ConfigValue("first-line-text")
        public static String firstLineText = "My Server";
        @ConfigValue("second-line-random")
        public static boolean randomMotd = true;
        @ConfigValue("second-line-text")
        public static String secondLineText = "This is a second line";

        private Motd() {
        }
    }

    public static class Afk {
        @ConfigValue("afk-seconds")
        public static int afktime = 60;
        @ConfigValue("kick-time-seconds")
        public static int kickTime = 1800;

        private Afk() {
        }
    }

    public static class Chat {
        @ConfigValue("chat-enabled")
        public static boolean enabled = true;
        @ConfigValue("chat-permission")
        public static boolean chatPermission = false;
        @ConfigValue("color-chat-permission")
        public static boolean colorPermission = true;
        @ConfigValue("format")
        public static String format = "%prefix%player%suffix: %message";
        @ConfigValue("multiple-prefix")
        public static boolean multiplePrefix = true;
        @ConfigValue("multiple-suffix")
        public static boolean multipleSuffix = true;

        private Chat() {
        }
    }

    public static class Rtp {
        @ConfigValue("min-distance")
        public static int rtpMin = 100;
        @ConfigValue("max-distance")
        public static int rtpMax = 10000;
        @ConfigValue("x-origin")
        public static int rtpOriginX = 0;
        @ConfigValue("z-origin")
        public static int rtpOriginZ = 0;

        private Rtp() {
        }
    }

    public ConfigManager get() {
        return config;
    }
}