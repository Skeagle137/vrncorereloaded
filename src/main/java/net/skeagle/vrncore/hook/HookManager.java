package net.skeagle.vrncore.hook;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import static org.bukkit.Bukkit.getServer;

public final class HookManager {

    private static VaultHook vault;

    private HookManager() {
    }

    public static void loadHooks() {
        if (checkPlugin("Vault"))
            vault = new VaultHook();
    }

    private static boolean checkPlugin(final String s) {
        final Plugin p = getServer().getPluginManager().getPlugin(s);
        return p != null && p.isEnabled();
    }

    public static boolean isVaultLoaded() {
        return vault != null;
    }

    public static String format(final Player p) {
        return isVaultLoaded() ? vault.format(p) : "";
    }

    public static String format(final String s, final Player p) {
        return isVaultLoaded() ? vault.format(s, p) : "";
    }
}
