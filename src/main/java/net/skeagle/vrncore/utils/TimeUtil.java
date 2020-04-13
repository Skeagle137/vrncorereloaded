package net.skeagle.vrncore.utils;

import org.mineacademy.fo.settings.YamlConfig;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

import static org.mineacademy.fo.settings.YamlConfig.TimeHelper.from;

public class TimeUtil {


    public static String timeToMessage(final long time) {
        final long days = time / 86400L;
        final long hours = (time % 86400L) / 3600L;
        final long minutes = (time % 86400L % 3600L) / 60L;
        final long seconds = time % 86400L % 3600L % 60L;
        final TimeUtil util = new TimeUtil();
        return ((days != 0) ? days + " " + util.timeGrammarCheck("day", days) + (hours != 0 || minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((hours != 0) ? hours + " " + util.timeGrammarCheck("hour", hours) + (minutes != 0 || seconds != 0 ? ", " : "") : "") +
                ((minutes != 0) ? minutes + " " + util.timeGrammarCheck("minute", minutes) + (seconds != 0 ? ", " : "") : "") +
                ((seconds != 0) ? seconds + " " + util.timeGrammarCheck("second", seconds) : "");
    }

    public static String timeToMessage(final int time) {
        return timeToMessage((long) time);
    }

    private String timeGrammarCheck(final String s, final long i) {
        if (i != 1) {
            return s + "s";
        }
        return s;
    }

    public static YamlConfig.TimeHelper fromSeconds(final long seconds) {
        return from(seconds + " seconds");
    }

    private enum TimeUnits {
        s(1),
        m(60),
        h(3600),
        d(86400),
        w(604800),
        y(31556926);

        private final long time_seconds;

        TimeUnits(final long time_seconds) {
            this.time_seconds = time_seconds;
        }
    }

    public static class TimeString {
        private static final Pattern ALLOWED_INPUT = Pattern.compile("[smhdwy]");
        long time;
        char unit;

        public TimeString(final long time, final char unit) {
            this.time = time;
            this.unit = unit;
        }

        public static long toSeconds(final TimeString ts) {
            long inseconds = 0;
            for (final TimeUnits unit : TimeUnits.values()) {
                if (ts.unit == unit.name().charAt(0)) {
                    inseconds = unit.time_seconds;
                }
            }
            return ts.time * inseconds;
        }

        public static TimeString toTimeString(final String s) throws TimeFormatException {
            switch (checkValid(s)) {
                case 0:
                    throw new TimeFormatException("&cThat is not a valid number.");
                case 1:
                    throw new TimeFormatException("&cUnknown time unit. Use s, m, h, d, w, or y to specify time.");
                case 2:
                    return new TimeString(Long.parseLong(s.substring(0, s.length() - 1)),
                            s.toLowerCase().charAt(s.length() - 1));
            }
            return null;
        }

        private static boolean isTimeValid(final String s) {
            try {
                Long.parseLong(s);
            } catch (final NumberFormatException e) {
                return false;
            }
            return true;
        }

        private static int checkValid(final String s) {
            if (!isTimeValid(s.substring(0, s.length() - 1))) {
                return 0;
            }
            if (!isUnitValid(s.substring(s.length() - 1))) {
                return 1;
            }
            return 2;
        }

        private static boolean isUnitValid(final String s) {
            return ALLOWED_INPUT.matcher(String.valueOf(s)).matches();
        }

        public static boolean isTimeString(final String[] args, final int index) throws TimeFormatException {
            if (index > args.length) {
                return false;
            }
            if (checkValid(args[index]) != 2) {
                return false;
            }
            toTimeString(args[index]);
            return true;
        }

        public static boolean checkDuplicates(final String s) {
            final Map<Character, Integer> chars = new HashMap<>();
            final char[] letters = s.toCharArray();
            int letter_count;
            for (final char c : letters) {
                if (chars.containsKey(c)) {
                    letter_count = chars.get(c);
                    chars.put(c, (letter_count + 1));
                } else {
                    chars.put(c, 1);
                }
            }
            for (final char c : chars.keySet()) {
                if (chars.get(c) > 1) {
                    return false;
                }
            }
            return true;
        }
    }
}
