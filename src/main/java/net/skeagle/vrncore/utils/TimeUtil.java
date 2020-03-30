package net.skeagle.vrncore.utils;

public class TimeUtil {
    public static String timeToMessage(final int time) {
        final long days = time / 86400L;
        final long hours = (time % 86400L) / 3600L;
        final long minutes = (time % 86400L % 3600L) / 60L;
        final long seconds = time % 86400L % 3600L % 60L;
        return ((days != 0) ? days + " " + timeGrammarCheck("day", days) + (hours != 0 || minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((hours != 0) ? hours + " " + timeGrammarCheck("hour", hours) + (minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((minutes != 0) ? minutes + " " + timeGrammarCheck("minute", minutes) + (seconds != 0 ? ", " : "") : "") +
                ((seconds != 0) ? seconds + " " + timeGrammarCheck("second", seconds) : "");
    }

    private static String timeGrammarCheck(final String s, final long i) {
        if (i != 1) {
            return s + "s";
        }
        return s;
    }
}
