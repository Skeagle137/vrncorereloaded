package net.skeagle.vrncore.utils;

import lombok.Getter;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

public class TimeUtil {
    private final Pattern ALLOWED_INPUT = Pattern.compile("[smhdwy]");
    private char c;


    public static String timeToMessage(final int time) {
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

    private String timeGrammarCheck(final String s, final long i) {
        if (i != 1) {
            return s + "s";
        }
        return s;
    }

    public static long toSeconds(String s) throws TimeFormatException {
        final TimeUtil util = new TimeUtil();
        try {
            s = util.checkValid(s);
        } catch (final TimeFormatException ex) {
            throw new TimeFormatException(ex.getMessage());
        }
        long inseconds = 0;
        for (final TimeUnits unit : TimeUnits.values()) {
            if (s.substring(s.length() - 1).equalsIgnoreCase(unit.name())) {
                inseconds = unit.time_seconds;
            }
        }
        return Long.parseLong(s.substring(0, s.length() - 1)) * inseconds;
    }

    private String checkValid(final String s) throws TimeFormatException {
        final char timeunit = s.charAt(s.length() - 1);
        if (!ALLOWED_INPUT.matcher(String.valueOf(timeunit)).matches()) {
            throw new TimeFormatException("&cPlease use a valid time unit (s, m, h, d, w, y).");
        }
        long time;
        try {
            time = Long.parseLong(s.substring(0, s.length() - 1));
        } catch (final NumberFormatException e) {
            throw new TimeFormatException("&cThat is not a valid number.");
        }
        if (time < 0) {
            time = 0;
        }
        if (!s.substring(0, s.length() - 1).matches("[0-9]") || s.length() < 2) {
            throw new TimeFormatException("&cYou must include a valid time input, e.g. \"10m\" (10 minutes).");
        }
        return String.valueOf(time) + timeunit;
    }

    private boolean checkDuplicates(final String s) {
        final Map<Character, Integer> chars = new HashMap<>();
        final char[] letters = s.toCharArray();
        int letter_count;
        for (final char c : letters) {
            if (chars.containsKey(c)) {
                letter_count = chars.get(c);
                chars.put(c, letter_count++);
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

    private enum TimeUnits {
        s(1),
        m(60),
        h(3600),
        d(86400),
        w(604800),
        y(31556926);

        @Getter
        private final long time_seconds;

        TimeUnits(final long time_seconds) {
            this.time_seconds = time_seconds;
        }
    }
}
