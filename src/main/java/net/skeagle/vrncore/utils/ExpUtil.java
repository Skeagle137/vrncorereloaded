package net.skeagle.vrncore.utils;

import org.bukkit.entity.Player;

public final class ExpUtil {

    public static int getExp(final Player player) {
        return getExpFromLevel(player.getLevel())
                + Math.round(getExpToNext(player.getLevel()) * player.getExp());
    }

    public static int getExpFromLevel(final int level) {
        if (level > 30) {
            return (int) (4.5 * level * level - 162.5 * level + 2220);
        }
        if (level > 15) {
            return (int) (2.5 * level * level - 40.5 * level + 360);
        }
        return level * level + 6 * level;
    }

    public static double getLevelFromExp(final double exp) {
        if (exp > 1395) {
            return (Math.sqrt(72 * exp - 54215) + 325) / 18;
        }
        if (exp > 315) {
            return Math.sqrt(40 * exp - 7839) / 10 + 8.1;
        }
        if (exp > 0) {
            return Math.sqrt(exp + 9) - 3;
        }
        return 0;
    }

    public static int getExpToNext(final int level) {
        if (level > 30) {
            return 9 * level - 158;
        }
        if (level > 15) {
            return 5 * level - 38;
        }
        return 2 * level + 7;
    }

    public static void changeExp(final Player player, int exp) {
        exp += getExp(player);

        if (exp < 0) {
            exp = 0;
        }

        final double levelAndExp = getLevelFromExp(exp);

        final int level = (int) levelAndExp;
        player.setLevel(level);
        player.setExp((float) (levelAndExp - level));
    }
}
