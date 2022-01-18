package net.skeagle.vrncore.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public final class HookManager {

    private static VaultHook vault;
    private static LuckPermsHook luckperms;

    public static void loadHooks() {
        if (checkPlugin("Vault"))
            vault = new VaultHook();
        if (checkPlugin("LuckPerms"))
            luckperms = new LuckPermsHook();
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

    public static String format(Player p) {
        return format(null, p);
    }

    public static String format(String s, Player p) {
        return isVaultLoaded() ? vault.format(s, p) : "";
    }
}
