package net.skeagle.vrncore.hook;

import net.skeagle.vrncore.playerdata.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public final class HookManager {

    private static VaultHook vault;
    private static LuckPermsHook luckperms;
    private static SuperVanishHook superVanish;

    public static void loadHooks() {
        if (checkPlugin("Vault"))
            vault = new VaultHook();
        if (checkPlugin("LuckPerms"))
            luckperms = new LuckPermsHook();
        if (checkPlugin("SuperVanish") || checkPlugin("PremiumVanish"))
            superVanish = new SuperVanishHook();
    }

    private static boolean checkPlugin(String s) {
        Plugin p = getServer().getPluginManager().getPlugin(s);
        return p != null && p.isEnabled();
    }

    public static boolean isVaultLoaded() {
        return vault != null;
    }

    public static boolean isLuckPermsLoaded() {
        return luckperms != null;
    }

    public static boolean isSuperVanishLoaded() {
        return superVanish != null;
    }

    public static String format(PlayerData data) {
        return format(data, null);
    }

    public static String format(PlayerData data, String s) {
        return isVaultLoaded() ? vault.format(data, s) : "";
    }
}
